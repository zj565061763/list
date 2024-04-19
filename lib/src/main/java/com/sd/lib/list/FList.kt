package com.sd.lib.list

interface FList<T> {

    /** 数据 */
    val data: List<T>

    /**
     * 设置数据
     */
    fun set(list: List<T>)

    /**
     * 清空数据
     */
    fun clear(): Boolean

    /**
     * 添加数据
     *
     * @return true-本次调用数据发生了变化
     */
    fun add(data: T): Boolean

    /**
     * 添加数据并根据[distinct]去重，删除[FList]中重复的数据
     *
     * @param list 新数据
     * @param distinct null-不进行去重操作，[distinct]返回true表示数据重复
     * @return true-本次调用数据发生了变化
     */
    fun addAll(
        list: List<T>,
        /** 去重条件，返回true表示数据重复 */
        distinct: ((oldItem: T, newItem: T) -> Boolean)?,
    ): Boolean

    /**
     * 添加数据并根据[distinct]去重，删除[list]中重复的数据
     *
     * @param list 新数据
     * @param distinct [distinct]返回true表示数据重复
     */
    fun addAllDistinctInput(
        list: List<T>,
        /** 去重条件，返回true表示数据重复 */
        distinct: (oldItem: T, newItem: T) -> Boolean = { oldItem, newItem -> oldItem == newItem },
    ): Boolean

    /**
     * 如果[block]返回的新对象 != 原对象，则用新对象替换原对象并结束遍历
     *
     * @return true-本次调用数据发生了变化
     */
    fun replaceFirst(block: (T) -> T): Boolean

    /**
     * 如果[block]返回的新对象 != 原对象，则用新对象替换原对象
     *
     * @return true-本次调用数据发生了变化
     */
    fun replaceAll(block: (T) -> T): Boolean

    /**
     * 删除第一个[predicate]为true的数据
     *
     * @return true-本次调用数据发生了变化
     */
    fun removeFirst(predicate: (T) -> Boolean): Boolean

    /**
     * 删除所有[predicate]为true的数据
     *
     * @return true-本次调用数据发生了变化
     */
    fun removeAll(predicate: (T) -> Boolean): Boolean
}

/**
 * 创建[FList]，
 * 不支持多线程并发，如果有多线程的使用场景，需要外部做线程同步。
 *
 * @param onChange 数据变化回调
 */
fun <T> FList(
    onChange: FList<T>.() -> Unit = {},
): FList<T> {
    return FListImpl(
        onChange = onChange,
    )
}

private class FListImpl<T>(
    private val onChange: FList<T>.() -> Unit = {},
) : FList<T> {

    private val _mutableList: MutableList<T> = mutableListOf()

    private var _isDirty = false
    private lateinit var _data: List<T>

    override val data: List<T>
        get() {
            if (_isDirty) {
                _isDirty = false
                _data = _mutableList.toList()
            }
            return _data
        }

    override fun set(list: List<T>) {
        modify { mutableList ->
            mutableList.clear()
            mutableList.addAll(list)
        }
    }

    override fun clear(): Boolean {
        return modify { mutableList ->
            val oldSize = mutableList.size
            mutableList.clear()
            oldSize > 0
        }
    }

    override fun add(data: T): Boolean {
        return modify { mutableList ->
            mutableList.add(data)
        }
    }

    override fun addAll(
        list: List<T>,
        distinct: ((oldItem: T, newItem: T) -> Boolean)?,
    ): Boolean {
        if (list.isEmpty()) return false

        if (distinct == null) {
            return modify { mutableList ->
                mutableList.addAll(list)
            }
        }

        return modify { mutableList ->
            val removeAllChanged = mutableList.removeAll { oldItem ->
                list.firstOrNull { newItem -> distinct(oldItem, newItem) } != null
            }
            val addAllChanged = mutableList.addAll(list)
            removeAllChanged || addAllChanged
        }
    }

    override fun addAllDistinctInput(
        list: List<T>,
        distinct: (oldItem: T, newItem: T) -> Boolean,
    ): Boolean {
        if (list.isEmpty()) return false
        return modify { mutableList ->
            val inputList = list.toMutableList()
            inputList.removeAll { newItem ->
                mutableList.firstOrNull { oldItem -> distinct(oldItem, newItem) } != null
            }
            mutableList.addAll(inputList)
        }
    }

    override fun replaceFirst(block: (T) -> T): Boolean {
        return modify { mutableList ->
            var result = false
            for (index in mutableList.indices) {
                val item = mutableList[index]
                val newItem = block(item)
                if (newItem != item) {
                    mutableList[index] = newItem
                    result = true
                    break
                }
            }
            result
        }
    }

    override fun replaceAll(block: (T) -> T): Boolean {
        return modify { mutableList ->
            var result = false
            for (index in mutableList.indices) {
                val item = mutableList[index]
                val newItem = block(item)
                if (newItem != item) {
                    mutableList[index] = newItem
                    result = true
                }
            }
            result
        }
    }

    override fun removeFirst(predicate: (T) -> Boolean): Boolean {
        return modify { mutableList ->
            mutableList.removeFirst(predicate)
        }
    }

    override fun removeAll(predicate: (T) -> Boolean): Boolean {
        return modify { mutableList ->
            mutableList.removeAll(predicate)
        }
    }

    private fun modify(block: (list: MutableList<T>) -> Boolean): Boolean {
        return block(_mutableList).also { change ->
            if (change) {
                _isDirty = true
                onChange()
            }
        }
    }
}

/**
 * 根据条件移除元素
 */
private fun <T> MutableList<T>.removeFirst(
    predicate: (T) -> Boolean,
): Boolean {
    with(iterator()) {
        while (hasNext()) {
            if (predicate(next())) {
                remove()
                return true
            }
        }
    }
    return false
}