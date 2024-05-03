package com.sd.lib.list

/**
 * 返回线程安全的[FList]
 */
fun <T> FList<T>.synchronizedList(): FList<T> {
    return if (this is SynchronizedList) this else SynchronizedList(this)
}

private class SynchronizedList<T>(
    private val proxy: FList<T>,
) : FList<T> {

    private val _lock = this

    override val data: List<T>
        get() = synchronized(_lock) {
            proxy.data
        }

    override fun set(elements: Collection<T>): Boolean {
        return synchronized(_lock) {
            proxy.set(elements)
        }
    }

    override fun clear(): Boolean {
        return synchronized(_lock) {
            proxy.clear()
        }
    }

    override fun add(element: T): Boolean {
        return synchronized(_lock) {
            proxy.add(element)
        }
    }

    override fun addAll(
        elements: Collection<T>,
        distinct: ((oldItem: T, newItem: T) -> Boolean)?,
    ): Boolean {
        return synchronized(_lock) {
            proxy.addAll(elements, distinct)
        }
    }

    override fun addAllDistinctInput(
        elements: Collection<T>,
        distinct: ((oldItem: T, newItem: T) -> Boolean)?,
    ): Boolean {
        return synchronized(_lock) {
            proxy.addAllDistinctInput(elements, distinct)
        }
    }

    override fun replaceFirst(block: (T) -> T): Boolean {
        return synchronized(_lock) {
            proxy.replaceFirst(block)
        }
    }

    override fun replaceAll(block: (T) -> T): Boolean {
        return synchronized(_lock) {
            proxy.replaceAll(block)
        }
    }

    override fun removeFirst(predicate: (T) -> Boolean): Boolean {
        return synchronized(_lock) {
            proxy.removeFirst(predicate)
        }
    }

    override fun removeAll(predicate: (T) -> Boolean): Boolean {
        return synchronized(_lock) {
            proxy.removeAll(predicate)
        }
    }

    override fun insert(index: Int, element: T): Boolean {
        return synchronized(_lock) {
            proxy.insert(index, element)
        }
    }

    override fun insertAll(
        index: Int,
        elements: Collection<T>,
        distinct: ((oldItem: T, newItem: T) -> Boolean)?,
    ): Boolean {
        return synchronized(_lock) {
            proxy.insertAll(index, elements, distinct)
        }
    }

    override fun insertAllDistinctInput(
        index: Int,
        elements: Collection<T>,
        distinct: ((oldItem: T, newItem: T) -> Boolean)?,
    ): Boolean {
        return synchronized(_lock) {
            proxy.insertAllDistinctInput(index, elements, distinct)
        }
    }
}