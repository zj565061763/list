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
    fun clear()

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
 * 创建[FList]
 *
 * @param initial 初始值
 */
fun <T> FList(
    initial: List<T> = emptyList(),
): FList<T> {
    return FListImpl(initial)
}

private class FListImpl<T>(
    initial: List<T>,
) : FList<T> {

    private val _list: MutableList<T> = mutableListOf<T>().apply {
        this.addAll(initial)
    }

    override val data: List<T> get() = _list

    override fun set(list: List<T>) {
        _list.clear()
        _list.addAll(list)
    }

    override fun clear() {
        _list.clear()
    }

    override fun add(data: T): Boolean {
        return _list.add(data)
    }

    override fun addAll(
        list: List<T>,
        distinct: ((oldItem: T, newItem: T) -> Boolean)?,
    ): Boolean {
        if (list.isEmpty()) return false
        if (distinct == null) {
            return _list.addAll(list)
        }
        val removeAllChanged = _list.removeAll { oldItem ->
            list.firstOrNull { newItem -> distinct(oldItem, newItem) } != null
        }
        val addAllChanged = _list.addAll(list)
        return removeAllChanged || addAllChanged
    }

    override fun addAllDistinctInput(
        list: List<T>,
        distinct: (oldItem: T, newItem: T) -> Boolean,
    ): Boolean {
        if (list.isEmpty()) return false
        val mutableList = list.toMutableList()
        mutableList.removeAll { newItem ->
            _list.firstOrNull { oldItem -> distinct(oldItem, newItem) } != null
        }
        return _list.addAll(mutableList)
    }

    override fun replaceFirst(block: (T) -> T): Boolean {
        var result = false
        for (index in _list.indices) {
            val item = _list[index]
            val newItem = block(item)
            if (newItem != item) {
                _list[index] = newItem
                result = true
                break
            }
        }
        return result
    }

    override fun replaceAll(block: (T) -> T): Boolean {
        var result = false
        for (index in _list.indices) {
            val item = _list[index]
            val newItem = block(item)
            if (newItem != item) {
                _list[index] = newItem
                result = true
            }
        }
        return result
    }

    override fun removeFirst(predicate: (T) -> Boolean): Boolean {
        return _list.removeFirst(predicate)
    }

    override fun removeAll(predicate: (T) -> Boolean): Boolean {
        return _list.removeAll(predicate)
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