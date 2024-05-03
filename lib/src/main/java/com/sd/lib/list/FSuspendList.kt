package com.sd.lib.list

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext

interface FSuspendList<T> {

    /** 数据 */
    val data: List<T>

    /**
     * 设置数据
     *
     * @return true-本次调用数据发生了变化
     */
    suspend fun set(elements: Collection<T>): Boolean

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

    /**
     * 在[FSuspendList]的调度器上面执行
     */
    suspend fun <R> dispatch(block: suspend FSuspendList<T>.() -> R): R
}

/**
 * 创建[FSuspendList]
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

    private val _rawList = FList(distinct = distinct)

    override val data: List<T>
        get() = _rawList.data

    override suspend fun set(elements: Collection<T>): Boolean {
        return dispatch {
            _rawList.set(elements)
        }
    }

    override suspend fun clear(): Boolean {
        return dispatch {
            _rawList.clear()
        }
    }

    override suspend fun add(data: T): Boolean {
        return dispatch {
            _rawList.add(data)
        }
    }

    override suspend fun addAll(list: List<T>): Boolean {
        return dispatch {
            _rawList.addAll(list)
        }
    }

    override suspend fun addAllDistinctInput(list: List<T>): Boolean {
        return dispatch {
            _rawList.addAllDistinctInput(list)
        }
    }

    override suspend fun replaceFirst(block: (T) -> T): Boolean {
        return dispatch {
            _rawList.replaceFirst(block)
        }
    }

    override suspend fun replaceAll(block: (T) -> T): Boolean {
        return dispatch {
            _rawList.replaceAll(block)
        }
    }

    override suspend fun removeFirst(predicate: (T) -> Boolean): Boolean {
        return dispatch {
            _rawList.removeFirst(predicate)
        }
    }

    override suspend fun removeAll(predicate: (T) -> Boolean): Boolean {
        return dispatch {
            _rawList.removeAll(predicate)
        }
    }

    override suspend fun <R> dispatch(block: suspend FSuspendList<T>.() -> R): R {
        return withContext(_dispatcher) {
            block()
        }
    }
}