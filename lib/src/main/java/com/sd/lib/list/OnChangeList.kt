package com.sd.lib.list

internal class OnChangeList<T>(
    private val proxy: FList<T>,
    private val onChange: () -> Unit,
) : FList<T> {

    override val data: List<T>
        get() = proxy.data

    override fun set(elements: Collection<T>): Boolean {
        return proxy.set(elements).also { if (it) onChange() }
    }

    override fun clear(): Boolean {
        return proxy.clear().also { if (it) onChange() }
    }

    override fun add(element: T): Boolean {
        return proxy.add(element).also { if (it) onChange() }
    }

    override fun addAll(
        elements: Collection<T>,
        distinct: ((oldItem: T, newItem: T) -> Boolean)?,
    ): Boolean {
        return proxy.addAll(elements, distinct).also { if (it) onChange() }
    }

    override fun addAllDistinctInput(
        elements: Collection<T>,
        distinct: ((oldItem: T, newItem: T) -> Boolean)?,
    ): Boolean {
        return proxy.addAllDistinctInput(elements, distinct).also { if (it) onChange() }
    }

    override fun replaceFirst(block: (T) -> T): Boolean {
        return proxy.replaceFirst(block).also { if (it) onChange() }
    }

    override fun replaceAll(block: (T) -> T): Boolean {
        return proxy.replaceAll(block).also { if (it) onChange() }
    }

    override fun removeFirst(predicate: (T) -> Boolean): Boolean {
        return proxy.removeFirst(predicate).also { if (it) onChange() }
    }

    override fun removeAll(predicate: (T) -> Boolean): Boolean {
        return proxy.removeAll(predicate).also { if (it) onChange() }
    }

    override fun insert(index: Int, element: T): Boolean {
        return proxy.insert(index, element).also { if (it) onChange() }
    }

    override fun insertAll(
        index: Int,
        elements: Collection<T>,
        distinct: ((oldItem: T, newItem: T) -> Boolean)?,
    ): Boolean {
        return proxy.insertAll(index, elements, distinct).also { if (it) onChange() }
    }
}