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
    suspend fun add(element: T): Boolean

    /**
     * 添加数据并根据[distinct]删除[FList]中重复的数据
     *
     * @return true-本次调用数据发生了变化
     */
    suspend fun addAll(
        elements: Collection<T>,
        distinct: ((oldItem: T, newItem: T) -> Boolean)? = { oldItem, newItem -> oldItem == newItem },
    ): Boolean

    /**
     * 添加数据并根据[distinct]删除[elements]中重复的数据
     *
     * @return true-本次调用数据发生了变化
     */
    suspend fun addAllDistinctInput(
        elements: Collection<T>,
        distinct: ((oldItem: T, newItem: T) -> Boolean)? = { oldItem, newItem -> oldItem == newItem },
    ): Boolean

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
     * 在[index]位置插入[element]
     *
     * @return true-本次调用数据发生了变化
     */
    suspend fun insert(index: Int, element: T): Boolean

    /**
     * 在[index]位置插入[elements]并根据[distinct]删除[FList]中重复的数据
     *
     * @return true-本次调用数据发生了变化
     */
    suspend fun insertAll(
        index: Int,
        elements: Collection<T>,
        distinct: ((oldItem: T, newItem: T) -> Boolean)? = { oldItem, newItem -> oldItem == newItem },
    ): Boolean

    /**
     * 在[index]位置插入[elements]并根据[distinct]删除[elements]中重复的数据
     *
     * @return true-本次调用数据发生了变化
     */
    suspend fun insertAllDistinctInput(
        index: Int,
        elements: Collection<T>,
        distinct: ((oldItem: T, newItem: T) -> Boolean)? = { oldItem, newItem -> oldItem == newItem },
    ): Boolean

    /**
     * 在[FSuspendList]的调度器上面执行
     */
    suspend fun <R> dispatch(block: suspend FSuspendList<T>.() -> R): R
}

/**
 * 创建[FSuspendList]
 *
 * @param dispatcher 调度器
 */
fun <T> FSuspendList(
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
): FSuspendList<T> {
    return SuspendListImpl(
        dispatcher = dispatcher,
    )
}

private class SuspendListImpl<T>(
    dispatcher: CoroutineDispatcher,
) : FSuspendList<T> {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _dispatcher = dispatcher.limitedParallelism(1)

    private val _rawList = FList<T>()

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

    override suspend fun add(element: T): Boolean {
        return dispatch {
            _rawList.add(element)
        }
    }

    override suspend fun addAll(
        elements: Collection<T>,
        distinct: ((oldItem: T, newItem: T) -> Boolean)?,
    ): Boolean {
        return dispatch {
            _rawList.addAll(elements, distinct)
        }
    }

    override suspend fun addAllDistinctInput(
        elements: Collection<T>,
        distinct: ((oldItem: T, newItem: T) -> Boolean)?,
    ): Boolean {
        return dispatch {
            _rawList.addAllDistinctInput(elements, distinct)
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

    override suspend fun insert(index: Int, element: T): Boolean {
        return dispatch {
            _rawList.insert(index, element)
        }
    }

    override suspend fun insertAll(
        index: Int,
        elements: Collection<T>,
        distinct: ((oldItem: T, newItem: T) -> Boolean)?,
    ): Boolean {
        return dispatch {
            _rawList.insertAll(index, elements, distinct)
        }
    }

    override suspend fun insertAllDistinctInput(
        index: Int,
        elements: Collection<T>,
        distinct: ((oldItem: T, newItem: T) -> Boolean)?,
    ): Boolean {
        return dispatch {
            _rawList.insertAllDistinctInput(index, elements, distinct)
        }
    }

    override suspend fun <R> dispatch(block: suspend FSuspendList<T>.() -> R): R {
        return withContext(_dispatcher) {
            block()
        }
    }
}