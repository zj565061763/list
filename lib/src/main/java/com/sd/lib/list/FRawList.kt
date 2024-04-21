package com.sd.lib.list

/**
 * 创建[FList]
 *
 * @param mutableList 要修改的列表，内部会保存这个列表并直接操作它
 * @param distinct 返回true表示两个对象相同，默认采用equals()比较
 */
fun <T> FRawList(
    mutableList: MutableList<T> = mutableListOf(),
    distinct: ((oldItem: T, newItem: T) -> Boolean)? = { oldItem, newItem -> oldItem == newItem },
): FList<T> {
    return RawListImpl(
        mutableList = mutableList,
        distinct = distinct,
    )
}

private class RawListImpl<T>(
    private val mutableList: MutableList<T>,
    private val distinct: ((oldItem: T, newItem: T) -> Boolean)?,
) : FList<T> {

    override val data: List<T>
        get() = mutableList

    override fun set(list: List<T>): Boolean {
        val clear = clear()
        val addAll = mutableList.addAll(list)
        return clear || addAll
    }

    override fun clear(): Boolean {
        val oldSize = mutableList.size
        mutableList.clear()
        return oldSize > 0
    }

    override fun add(data: T): Boolean {
        return mutableList.add(data)
    }

    override fun addAll(list: List<T>): Boolean {
        if (list.isEmpty()) return false
        val dist = distinct
        return if (dist == null) {
            mutableList.addAll(list)
        } else {
            val removeAllChanged = mutableList.removeAll { oldItem ->
                list.find { newItem -> dist(oldItem, newItem) } != null
            }
            val addAllChanged = mutableList.addAll(list)
            removeAllChanged || addAllChanged
        }
    }

    override fun addAllDistinctInput(list: List<T>): Boolean {
        if (list.isEmpty()) return false
        val dist = distinct
        return if (dist == null) {
            mutableList.addAll(list)
        } else {
            val inputList = list.toMutableList()
            inputList.removeAll { newItem ->
                mutableList.find { oldItem -> dist(oldItem, newItem) } != null
            }
            mutableList.addAll(inputList)
        }
    }

    override fun replaceFirst(block: (T) -> T): Boolean {
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
        return result
    }

    override fun replaceAll(block: (T) -> T): Boolean {
        var result = false
        for (index in mutableList.indices) {
            val item = mutableList[index]
            val newItem = block(item)
            if (newItem != item) {
                mutableList[index] = newItem
                result = true
            }
        }
        return result
    }

    override fun removeFirst(predicate: (T) -> Boolean): Boolean {
        return mutableList.removeFirst(predicate)
    }

    override fun removeAll(predicate: (T) -> Boolean): Boolean {
        return mutableList.removeAll(predicate)
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