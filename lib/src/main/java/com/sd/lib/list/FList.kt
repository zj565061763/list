package com.sd.lib.list

import java.util.Collections
import java.util.concurrent.atomic.AtomicBoolean

interface FList<T> {

    /** 数据 */
    val data: List<T>

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
    fun add(data: T): Boolean

    /**
     * 添加数据并删除[FList]中重复的数据
     *
     * @return true-本次调用数据发生了变化
     */
    fun addAll(elements: Collection<T>): Boolean

    /**
     * 添加数据并删除[list]中重复的数据
     *
     * @param list 新数据
     * @return true-本次调用数据发生了变化
     */
    fun addAllDistinctInput(list: List<T>): Boolean

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

    /**
     * 在[index]位置插入[data]
     *
     * @return true-本次调用数据发生了变化
     */
    fun insert(index: Int, data: T): Boolean

    /**
     * 在[index]位置插入[list]
     *
     * @return true-本次调用数据发生了变化
     */
    fun insertAll(index: Int, list: List<T>): Boolean
}

/**
 * 创建[FList]
 *
 * @param distinct 返回true表示两个对象相同，默认采用equals()比较
 */
fun <T> FList(
    distinct: ((oldItem: T, newItem: T) -> Boolean)? = { oldItem, newItem -> oldItem == newItem },
): FList<T> {
    return ListImpl(
        distinct = distinct,
    )
}

private class ListImpl<T>(
    distinct: ((oldItem: T, newItem: T) -> Boolean)?,
) : FList<T> {

    private val _isDirty = AtomicBoolean(false)

    private val _rawList = OnChangeList(
        proxy = FRawList(
            mutableList = Collections.synchronizedList(mutableListOf()),
            distinct = distinct,
        ),
        onChange = { _isDirty.set(true) },
    )

    private var _data: List<T> = emptyList()

    override val data: List<T>
        get() {
            if (_isDirty.compareAndSet(true, false)) {
                _data = _rawList.data.toList()
            }
            return _data
        }

    override fun set(elements: Collection<T>): Boolean {
        return _rawList.set(elements)
    }

    override fun clear(): Boolean {
        return _rawList.clear()
    }

    override fun add(data: T): Boolean {
        return _rawList.add(data)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        return _rawList.addAll(elements)
    }

    override fun addAllDistinctInput(list: List<T>): Boolean {
        return _rawList.addAllDistinctInput(list)
    }

    override fun replaceFirst(block: (T) -> T): Boolean {
        return _rawList.replaceFirst(block)
    }

    override fun replaceAll(block: (T) -> T): Boolean {
        return _rawList.replaceAll(block)
    }

    override fun removeFirst(predicate: (T) -> Boolean): Boolean {
        return _rawList.removeFirst(predicate)
    }

    override fun removeAll(predicate: (T) -> Boolean): Boolean {
        return _rawList.removeAll(predicate)
    }

    override fun insert(index: Int, data: T): Boolean {
        return _rawList.insert(index, data)
    }

    override fun insertAll(index: Int, list: List<T>): Boolean {
        return _rawList.insertAll(index, list)
    }
}