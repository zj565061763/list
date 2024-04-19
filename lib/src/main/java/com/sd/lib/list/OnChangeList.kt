package com.sd.lib.list

internal class OnChangeList<T>(
    private val proxy: FList<T>,
    private val onChange: () -> Unit,
) : FList<T> {
    override val data: List<T>
        get() = proxy.data

    override fun set(list: List<T>): Boolean {
        return proxy.set(list).also { if (it) onChange() }
    }

    override fun clear(): Boolean {
        return proxy.clear().also { if (it) onChange() }
    }

    override fun add(data: T): Boolean {
        return proxy.add(data).also { if (it) onChange() }
    }

    override fun addAll(list: List<T>): Boolean {
        return proxy.addAll(list).also { if (it) onChange() }
    }

    override fun addAllDistinctInput(list: List<T>): Boolean {
        return proxy.addAllDistinctInput(list).also { if (it) onChange() }
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
}