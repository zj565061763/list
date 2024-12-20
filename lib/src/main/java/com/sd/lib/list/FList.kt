package com.sd.lib.list

interface FList<T> {
  /**
   * 数据
   */
  fun getData(): List<T>

  /**
   * 设置数据
   *
   * @return true-本次调用数据发生了变化
   */
  fun set(elements: Collection<T>): Boolean

  /**
   * 清空数据
   *
   * @return true-本次调用数据发生了变化
   */
  fun clear(): Boolean

  /**
   * 添加数据
   *
   * @return true-本次调用数据发生了变化
   */
  fun add(element: T): Boolean

  /**
   * 添加数据并根据[distinct]删除[FList]中重复的数据
   *
   * @return true-本次调用数据发生了变化
   */
  fun addAll(
    elements: Collection<T>,
    distinct: ((oldItem: T, newItem: T) -> Boolean)? = null,
  ): Boolean

  /**
   * 添加数据并根据[distinct]删除[elements]中重复的数据
   *
   * @return true-本次调用数据发生了变化
   */
  fun addAllDistinctInput(
    elements: Collection<T>,
    distinct: ((oldItem: T, newItem: T) -> Boolean)? = null,
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
   * 替换[index]位置的数据
   */
  fun replaceAt(index: Int, element: T): Boolean

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

  /**
   * 删除[index]位置的数据
   *
   * @return true-本次调用数据发生了变化
   */
  fun removeAt(index: Int): Boolean

  /**
   * 在[index]位置插入[element]
   *
   * @return true-本次调用数据发生了变化
   */
  fun insert(index: Int, element: T): Boolean

  /**
   * 在[index]位置插入[elements]并根据[distinct]删除[FList]中重复的数据
   *
   * @return true-本次调用数据发生了变化
   */
  fun insertAll(
    index: Int,
    elements: Collection<T>,
    distinct: ((oldItem: T, newItem: T) -> Boolean)? = null,
  ): Boolean

  /**
   * 在[index]位置插入[elements]并根据[distinct]删除[elements]中重复的数据
   *
   * @return true-本次调用数据发生了变化
   */
  fun insertAllDistinctInput(
    index: Int,
    elements: Collection<T>,
    distinct: ((oldItem: T, newItem: T) -> Boolean)? = null,
  ): Boolean

  /**
   * 修改
   */
  fun <R> modify(block: FList<T>.() -> R): R
}

/**
 * 创建[FList]
 */
fun <T> FList(
  distinct: ((oldItem: T, newItem: T) -> Boolean)? = null,
): FList<T> = ListImpl(distinct)

private class ListImpl<T>(
  private val distinct: ((oldItem: T, newItem: T) -> Boolean)?,
) : FList<T> {
  private var _isDirty = false
  private var _data: List<T> = emptyList()

  private val _list: FList<T> = object : OnChangeList<T>(RawList()) {
    override fun onChange() {
      _isDirty = true
    }
  }

  override fun getData(): List<T> {
    if (_isDirty) {
      _data = _list.getData().toList()
      _isDirty = false
    }
    return _data
  }

  override fun set(elements: Collection<T>): Boolean {
    return _list.set(elements)
  }

  override fun clear(): Boolean {
    return _list.clear()
  }

  override fun add(element: T): Boolean {
    return _list.add(element)
  }

  override fun addAll(
    elements: Collection<T>,
    distinct: ((oldItem: T, newItem: T) -> Boolean)?,
  ): Boolean {
    return _list.addAll(
      elements = elements,
      distinct = distinct ?: this.distinct,
    )
  }

  override fun addAllDistinctInput(
    elements: Collection<T>,
    distinct: ((oldItem: T, newItem: T) -> Boolean)?,
  ): Boolean {
    return _list.addAllDistinctInput(
      elements = elements,
      distinct = distinct ?: this.distinct,
    )
  }

  override fun replaceFirst(block: (T) -> T): Boolean {
    return _list.replaceFirst(block)
  }

  override fun replaceAll(block: (T) -> T): Boolean {
    return _list.replaceAll(block)
  }

  override fun replaceAt(index: Int, element: T): Boolean {
    return _list.replaceAt(index, element)
  }

  override fun removeFirst(predicate: (T) -> Boolean): Boolean {
    return _list.removeFirst(predicate)
  }

  override fun removeAll(predicate: (T) -> Boolean): Boolean {
    return _list.removeAll(predicate)
  }

  override fun removeAt(index: Int): Boolean {
    return _list.removeAt(index)
  }

  override fun insert(index: Int, element: T): Boolean {
    return _list.insert(index, element)
  }

  override fun insertAll(
    index: Int,
    elements: Collection<T>,
    distinct: ((oldItem: T, newItem: T) -> Boolean)?,
  ): Boolean {
    return _list.insertAll(
      index = index,
      elements = elements,
      distinct = distinct ?: this.distinct,
    )
  }

  override fun insertAllDistinctInput(
    index: Int,
    elements: Collection<T>,
    distinct: ((oldItem: T, newItem: T) -> Boolean)?,
  ): Boolean {
    return _list.insertAllDistinctInput(
      index = index,
      elements = elements,
      distinct = distinct ?: this.distinct,
    )
  }

  override fun <R> modify(block: FList<T>.() -> R): R {
    return block.invoke(this)
  }
}