package com.sd.lib.list

internal class OnChangeList<T>(
  private val proxy: FList<T>,
  private val onChange: () -> Unit,
) : FList<T> {

  override fun getData(): List<T> {
    return proxy.getData()
  }

  override fun set(elements: Collection<T>): Boolean {
    return proxy.set(elements).also(::notifyChange)
  }

  override fun clear(): Boolean {
    return proxy.clear().also(::notifyChange)
  }

  override fun add(element: T): Boolean {
    return proxy.add(element).also(::notifyChange)
  }

  override fun addAll(
    elements: Collection<T>,
    distinct: ((oldItem: T, newItem: T) -> Boolean)?,
  ): Boolean {
    return proxy.addAll(elements, distinct).also(::notifyChange)
  }

  override fun addAllDistinctInput(
    elements: Collection<T>,
    distinct: ((oldItem: T, newItem: T) -> Boolean)?,
  ): Boolean {
    return proxy.addAllDistinctInput(elements, distinct).also(::notifyChange)
  }

  override fun replaceFirst(block: (T) -> T): Boolean {
    return proxy.replaceFirst(block).also(::notifyChange)
  }

  override fun replaceAll(block: (T) -> T): Boolean {
    return proxy.replaceAll(block).also(::notifyChange)
  }

  override fun replaceAt(index: Int, element: T): Boolean {
    return proxy.replaceAt(index, element).also(::notifyChange)
  }

  override fun removeFirst(predicate: (T) -> Boolean): Boolean {
    return proxy.removeFirst(predicate).also(::notifyChange)
  }

  override fun removeAll(predicate: (T) -> Boolean): Boolean {
    return proxy.removeAll(predicate).also(::notifyChange)
  }

  override fun removeAt(index: Int): Boolean {
    return proxy.removeAt(index).also(::notifyChange)
  }

  override fun insert(index: Int, element: T): Boolean {
    return proxy.insert(index, element).also(::notifyChange)
  }

  override fun insertAll(
    index: Int,
    elements: Collection<T>,
    distinct: ((oldItem: T, newItem: T) -> Boolean)?,
  ): Boolean {
    return proxy.insertAll(index, elements, distinct).also(::notifyChange)
  }

  override fun insertAllDistinctInput(
    index: Int,
    elements: Collection<T>,
    distinct: ((oldItem: T, newItem: T) -> Boolean)?,
  ): Boolean {
    return proxy.insertAllDistinctInput(index, elements, distinct).also(::notifyChange)
  }

  override fun <R> modify(block: FList<T>.() -> R): R {
    return block.invoke(this)
  }

  private fun notifyChange(change: Boolean) {
    if (change) onChange()
  }
}