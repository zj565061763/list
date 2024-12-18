package com.sd.lib.list

internal class RawList<T> : FList<T> {
  private val _mutableList = mutableListOf<T>()

  override fun getData(): List<T> {
    return _mutableList
  }

  override fun set(elements: Collection<T>): Boolean {
    val clear = clear()
    val addAll = _mutableList.addAll(elements)
    return clear || addAll
  }

  override fun clear(): Boolean {
    val oldSize = _mutableList.size
    _mutableList.clear()
    return oldSize > 0
  }

  override fun add(element: T): Boolean {
    return _mutableList.add(element)
  }

  override fun addAll(
    elements: Collection<T>,
    distinct: ((oldItem: T, newItem: T) -> Boolean)?,
  ): Boolean {
    if (elements.isEmpty()) return false
    return if (distinct == null) {
      _mutableList.addAll(elements)
    } else {
      val removeAllChanged = _mutableList.removeAll { oldItem ->
        elements.find { newItem -> distinct(oldItem, newItem) } != null
      }
      val addAllChanged = _mutableList.addAll(elements)
      removeAllChanged || addAllChanged
    }
  }

  override fun addAllDistinctInput(
    elements: Collection<T>,
    distinct: ((oldItem: T, newItem: T) -> Boolean)?,
  ): Boolean {
    if (elements.isEmpty()) return false
    return if (distinct == null) {
      _mutableList.addAll(elements)
    } else {
      val inputList = elements.toMutableList()
      inputList.removeAll { newItem ->
        _mutableList.find { oldItem -> distinct(oldItem, newItem) } != null
      }
      _mutableList.addAll(inputList)
    }
  }

  override fun replaceFirst(block: (T) -> T): Boolean {
    var result = false
    for (index in _mutableList.indices) {
      val item = _mutableList[index]
      val newItem = block(item)
      if (newItem != item) {
        _mutableList[index] = newItem
        result = true
        break
      }
    }
    return result
  }

  override fun replaceAll(block: (T) -> T): Boolean {
    var result = false
    for (index in _mutableList.indices) {
      val item = _mutableList[index]
      val newItem = block(item)
      if (newItem != item) {
        _mutableList[index] = newItem
        result = true
      }
    }
    return result
  }

  override fun replaceAt(index: Int, element: T): Boolean {
    return _mutableList.set(index, element) != null
  }

  override fun removeFirst(predicate: (T) -> Boolean): Boolean {
    val index = _mutableList.indexOfFirst(predicate)
    return if (index < 0) {
      false
    } else {
      _mutableList.removeAt(index)
      true
    }
  }

  override fun removeAll(predicate: (T) -> Boolean): Boolean {
    return _mutableList.removeAll(predicate)
  }

  override fun removeAt(index: Int): Boolean {
    return _mutableList.removeAt(index) != null
  }

  override fun insert(index: Int, element: T): Boolean {
    _mutableList.add(index, element)
    return true
  }

  override fun insertAll(
    index: Int,
    elements: Collection<T>,
    distinct: ((oldItem: T, newItem: T) -> Boolean)?,
  ): Boolean {
    if (elements.isEmpty()) return false
    return if (distinct == null) {
      _mutableList.addAll(index, elements)
    } else {
      val removeAllChanged = _mutableList.removeAll { oldItem ->
        elements.find { newItem -> distinct(oldItem, newItem) } != null
      }
      val addAllChanged = _mutableList.addAll(index, elements)
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
      _mutableList.addAll(index, elements)
    } else {
      val inputList = elements.toMutableList()
      inputList.removeAll { newItem ->
        _mutableList.find { oldItem -> distinct(oldItem, newItem) } != null
      }
      _mutableList.addAll(index, inputList)
    }
  }

  override fun <R> modify(block: FList<T>.() -> R): R {
    return block.invoke(this)
  }
}