package com.sd.lib.list

internal class RawList<T> : FList<T> {
  private val _list = mutableListOf<T>()

  override fun getData(): List<T> {
    return _list
  }

  override fun set(elements: Collection<T>): Boolean {
    val clear = clear()
    val addAll = _list.addAll(elements)
    return clear || addAll
  }

  override fun clear(): Boolean {
    val oldSize = _list.size
    _list.clear()
    return oldSize > 0
  }

  override fun add(element: T): Boolean {
    return _list.add(element)
  }

  override fun addAll(
    elements: Collection<T>,
    distinct: ((oldItem: T, newItem: T) -> Boolean)?,
  ): Boolean {
    if (elements.isEmpty()) return false
    return if (distinct == null) {
      _list.addAll(elements)
    } else {
      val removeAllChanged = _list.removeAll { oldItem ->
        elements.find { newItem -> distinct(oldItem, newItem) } != null
      }
      val addAllChanged = _list.addAll(elements)
      removeAllChanged || addAllChanged
    }
  }

  override fun addAllDistinctInput(
    elements: Collection<T>,
    distinct: ((oldItem: T, newItem: T) -> Boolean)?,
  ): Boolean {
    if (elements.isEmpty()) return false
    return if (distinct == null) {
      _list.addAll(elements)
    } else {
      val inputList = elements.toMutableList()
      inputList.removeAll { newItem ->
        _list.find { oldItem -> distinct(oldItem, newItem) } != null
      }
      _list.addAll(inputList)
    }
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

  override fun replaceAt(index: Int, element: T): Boolean {
    return _list.set(index, element) != null
  }

  override fun removeFirst(predicate: (T) -> Boolean): Boolean {
    val index = _list.indexOfFirst(predicate)
    return if (index < 0) {
      false
    } else {
      _list.removeAt(index)
      true
    }
  }

  override fun removeAll(predicate: (T) -> Boolean): Boolean {
    return _list.removeAll(predicate)
  }

  override fun removeAt(index: Int): Boolean {
    return _list.removeAt(index) != null
  }

  override fun insert(index: Int, element: T): Boolean {
    _list.add(index, element)
    return true
  }

  override fun insertAll(
    index: Int,
    elements: Collection<T>,
    distinct: ((oldItem: T, newItem: T) -> Boolean)?,
  ): Boolean {
    if (elements.isEmpty()) return false
    return if (distinct == null) {
      _list.addAll(index, elements)
    } else {
      val removeAllChanged = _list.removeAll { oldItem ->
        elements.find { newItem -> distinct(oldItem, newItem) } != null
      }
      val addAllChanged = _list.addAll(index, elements)
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
      _list.addAll(index, elements)
    } else {
      val inputList = elements.toMutableList()
      inputList.removeAll { newItem ->
        _list.find { oldItem -> distinct(oldItem, newItem) } != null
      }
      _list.addAll(index, inputList)
    }
  }

  override fun <R> modify(block: FList<T>.() -> R): R {
    return block.invoke(this)
  }
}