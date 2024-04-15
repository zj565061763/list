package com.sd.lib.list

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

interface FSuspendList<T> {

    /** 数据 */
    val data: List<T>

    /** 数据流 */
    val dataFlow: Flow<List<T>>

    /**
     * 设置数据
     */
    suspend fun set(list: List<T>)

    /**
     * 清空数据
     */
    suspend fun clear()

    /**
     * 添加数据
     */
    suspend fun add(data: T)

    /**
     * 添加数据
     */
    suspend fun addAll(list: List<T>)

    /**
     * 添加数据并去重，删除[FSuspendList]中重复的数据
     */
    suspend fun addAllDistinct(
        list: List<T>,
        /** 去重条件，返回true表示数据重复 */
        distinct: (oldItem: T, newItem: T) -> Boolean = { oldItem, newItem -> oldItem == newItem },
    )

    /**
     * 添加数据并去重，删除[list]中重复的数据
     */
    suspend fun addAllDistinctInput(
        list: List<T>,
        /** 去重条件，返回true表示数据重复 */
        distinct: (oldItem: T, newItem: T) -> Boolean = { oldItem, newItem -> oldItem == newItem },
    )

    /**
     * 如果[block]返回的新对象 != 原对象，则用新对象替换原对象并结束遍历
     */
    suspend fun replaceFirst(block: (T) -> T)

    /**
     * 如果[block]返回的新对象 != 原对象，则用新对象替换原对象
     */
    suspend fun replaceAll(block: (T) -> T)

    /**
     * 删除第一个[predicate]为true的数据
     */
    suspend fun removeFirst(predicate: (T) -> Boolean)

    /**
     * 删除所有[predicate]为true的数据
     */
    suspend fun removeAll(predicate: (T) -> Boolean)

    /**
     * 修改数据
     */
    suspend fun modify(block: (list: MutableList<T>) -> Boolean)
}

/**
 * 创建[FSuspendList]
 *
 * @param initial 初始值
 * @param dispatcher 并发必须为1
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun <T> FSuspendList(
    initial: List<T> = emptyList(),
    dispatcher: CoroutineDispatcher = Dispatchers.Default.limitedParallelism(1)
): FSuspendList<T> {
    return FListImpl(
        initial = initial,
        dispatcher = dispatcher,
    )
}