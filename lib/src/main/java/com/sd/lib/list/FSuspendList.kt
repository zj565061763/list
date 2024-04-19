package com.sd.lib.list

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean

interface FSuspendList<T> {

    /** 数据 */
    val data: List<T>

    /**
     * 设置数据
     *
     * @return true-本次调用数据发生了变化
     */
    suspend fun set(list: List<T>): Boolean

    /**
     * 清空数据
     *
     * @return true-本次调用数据发生了变化
     */
    suspend fun clear(): Boolean

    /**
     * 添加数据
     *
     * @return true-本次调用数据发生了变化
     */
    suspend fun add(data: T): Boolean

    /**
     * 添加数据并删除[FList]中重复的数据
     *
     * @param list 新数据
     * @return true-本次调用数据发生了变化
     */
    suspend fun addAll(list: List<T>): Boolean

    /**
     * 添加数据并删除[list]中重复的数据
     *
     * @param list 新数据
     * @return true-本次调用数据发生了变化
     */
    suspend fun addAllDistinctInput(list: List<T>): Boolean

    /**
     * 如果[block]返回的新对象 != 原对象，则用新对象替换原对象并结束遍历
     *
     * @return true-本次调用数据发生了变化
     */
    suspend fun replaceFirst(block: (T) -> T): Boolean

    /**
     * 如果[block]返回的新对象 != 原对象，则用新对象替换原对象
     *
     * @return true-本次调用数据发生了变化
     */
    suspend fun replaceAll(block: (T) -> T): Boolean

    /**
     * 删除第一个[predicate]为true的数据
     *
     * @return true-本次调用数据发生了变化
     */
    suspend fun removeFirst(predicate: (T) -> Boolean): Boolean

    /**
     * 删除所有[predicate]为true的数据
     *
     * @return true-本次调用数据发生了变化
     */
    suspend fun removeAll(predicate: (T) -> Boolean): Boolean
}

/**
 * 创建[FSuspendList]，不支持多线程并发
 *
 * @param distinct 返回true表示两个对象相同，默认采用equals()比较
 */
fun <T> FSuspendList(
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    distinct: ((oldItem: T, newItem: T) -> Boolean)? = { oldItem, newItem -> oldItem == newItem },
): FSuspendList<T> {
    return SuspendListImpl(
        dispatcher = dispatcher,
        distinct = distinct,
    )
}

private class SuspendListImpl<T>(
    dispatcher: CoroutineDispatcher,
    distinct: ((oldItem: T, newItem: T) -> Boolean)?,
) : FSuspendList<T> {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _dispatcher = dispatcher.limitedParallelism(1)

    private val _isDirty = AtomicBoolean(false)

    private val _rawList = OnChangeList(
        proxy = FRawList(distinct = distinct),
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

    override suspend fun set(list: List<T>): Boolean {
        return withContext(_dispatcher) {
            _rawList.set(list)
        }
    }

    override suspend fun clear(): Boolean {
        return withContext(_dispatcher) {
            _rawList.clear()
        }
    }

    override suspend fun add(data: T): Boolean {
        return withContext(_dispatcher) {
            _rawList.add(data)
        }
    }

    override suspend fun addAll(list: List<T>): Boolean {
        return withContext(_dispatcher) {
            _rawList.addAll(list)
        }
    }

    override suspend fun addAllDistinctInput(list: List<T>): Boolean {
        return withContext(_dispatcher) {
            _rawList.addAllDistinctInput(list)
        }
    }

    override suspend fun replaceFirst(block: (T) -> T): Boolean {
        return withContext(_dispatcher) {
            _rawList.replaceFirst(block)
        }
    }

    override suspend fun replaceAll(block: (T) -> T): Boolean {
        return withContext(_dispatcher) {
            _rawList.replaceAll(block)
        }
    }

    override suspend fun removeFirst(predicate: (T) -> Boolean): Boolean {
        return withContext(_dispatcher) {
            _rawList.removeFirst(predicate)
        }
    }

    override suspend fun removeAll(predicate: (T) -> Boolean): Boolean {
        return withContext(_dispatcher) {
            _rawList.removeAll(predicate)
        }
    }
}