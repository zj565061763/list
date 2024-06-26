package com.sd.lib.list

/**
 * 创建[FList]
 *
 * @param mutableList 要修改的列表，内部会保存这个列表并直接操作它
 */
fun <T> FRawList(mutableList: MutableList<T> = mutableListOf()): FList<T> {
    return RawListImpl(mutableList)
}

private class RawListImpl<T>(
    private val mutableList: MutableList<T>,
) : FList<T> {

    override fun getData(): List<T> {
        return mutableList
    }

    override fun set(elements: Collection<T>): Boolean {
        val clear = clear()
        val addAll = mutableList.addAll(elements)
        return clear || addAll
    }

    override fun clear(): Boolean {
        val oldSize = mutableList.size
        mutableList.clear()
        return oldSize > 0
    }

    override fun add(element: T): Boolean {
        return mutableList.add(element)
    }

    override fun addAll(
        elements: Collection<T>,
        distinct: ((oldItem: T, newItem: T) -> Boolean)?,
    ): Boolean {
        if (elements.isEmpty()) return false
        return if (distinct == null) {
            mutableList.addAll(elements)
        } else {
            val removeAllChanged = mutableList.removeAll { oldItem ->
                elements.find { newItem -> distinct(oldItem, newItem) } != null
            }
            val addAllChanged = mutableList.addAll(elements)
            removeAllChanged || addAllChanged
        }
    }

    override fun addAllDistinctInput(
        elements: Collection<T>,
        distinct: ((oldItem: T, newItem: T) -> Boolean)?,
    ): Boolean {
        if (elements.isEmpty()) return false
        return if (distinct == null) {
            mutableList.addAll(elements)
        } else {
            val inputList = elements.toMutableList()
            inputList.removeAll { newItem ->
                mutableList.find { oldItem -> distinct(oldItem, newItem) } != null
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

    override fun replaceAt(index: Int, element: T): Boolean {
        return mutableList.set(index, element) != null
    }

    override fun removeFirst(predicate: (T) -> Boolean): Boolean {
        val index = mutableList.indexOfFirst(predicate)
        return if (index < 0) {
            false
        } else {
            mutableList.removeAt(index)
            true
        }
    }

    override fun removeAll(predicate: (T) -> Boolean): Boolean {
        return mutableList.removeAll(predicate)
    }

    override fun removeAt(index: Int): Boolean {
        return mutableList.removeAt(index) != null
    }

    override fun insert(index: Int, element: T): Boolean {
        mutableList.add(index, element)
        return true
    }

    override fun insertAll(
        index: Int,
        elements: Collection<T>,
        distinct: ((oldItem: T, newItem: T) -> Boolean)?,
    ): Boolean {
        if (elements.isEmpty()) return false
        return if (distinct == null) {
            mutableList.addAll(index, elements)
        } else {
            val removeAllChanged = mutableList.removeAll { oldItem ->
                elements.find { newItem -> distinct(oldItem, newItem) } != null
            }
            val addAllChanged = mutableList.addAll(index, elements)
            removeAllChanged || addAllChanged
        }
    }

    override fun insertAllDistinctInput(
        index: Int,
        elements: Collection<T>,
        distinct: ((oldItem: T, newItem: T) -> Boolean)?,
    ): Boolean {
        if (elements.isEmpty()) return false
        return if (distinct == null) {
            mutableList.addAll(index, elements)
        } else {
            val inputList = elements.toMutableList()
            inputList.removeAll { newItem ->
                mutableList.find { oldItem -> distinct(oldItem, newItem) } != null
            }
            mutableList.addAll(index, inputList)
        }
    }

    override fun <R> modify(block: FList<T>.() -> R): R {
        return block.invoke(this)
    }
}