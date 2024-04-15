package com.sd.lib.list

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

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
     *
     * @return true-本次调用数据发生了变化
     */
    suspend fun add(data: T): Boolean

    /**
     * 添加数据并根据[distinct]去重，删除[FList]中重复的数据
     *
     * @param list 新数据
     * @param distinct null-不进行去重操作，[distinct]返回true表示数据重复
     * @return true-本次调用数据发生了变化
     */
    suspend fun addAll(
        list: List<T>,
        /** 去重条件，返回true表示数据重复 */
        distinct: ((oldItem: T, newItem: T) -> Boolean)?,
    ): Boolean

    /**
     * 添加数据并根据[distinct]去重，删除[list]中重复的数据
     *
     * @param list 新数据
     * @param distinct [distinct]返回true表示数据重复
     */
    suspend fun addAllDistinctInput(
        list: List<T>,
        /** 去重条件，返回true表示数据重复 */
        distinct: (oldItem: T, newItem: T) -> Boolean,
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
     * 修改数据
     *
     * @param block [block]返回true表示数据修改成功
     * @return true-本次调用数据发生了变化
     */
    suspend fun modify(block: (list: MutableList<T>) -> Boolean): Boolean
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
    return FSuspendListImpl(
        initial = initial,
        dispatcher = dispatcher,
    )
}

//-------------------- impl --------------------

private class FSuspendListImpl<T>(
    initial: List<T>,
    private val dispatcher: CoroutineDispatcher,
) : FSuspendList<T> {

    private val _list = FList(initial)
    private val _dataFlow: MutableStateFlow<List<T>> = MutableStateFlow(initial)

    override val data: List<T> get() = _dataFlow.value
    override val dataFlow: Flow<List<T>> = _dataFlow.asStateFlow()

    override suspend fun set(list: List<T>) {
        modifyList {
            it.set(list)
            true
        }
    }

    override suspend fun clear() {
        modifyList {
            it.clear()
            true
        }
    }

    override suspend fun add(data: T): Boolean {
        return modifyList {
            it.add(data)
        }
    }

    override suspend fun addAll(
        list: List<T>,
        distinct: ((oldItem: T, newItem: T) -> Boolean)?,
    ): Boolean {
        return modifyList {
            it.addAll(
                list = list,
                distinct = distinct,
            )
        }
    }

    override suspend fun addAllDistinctInput(
        list: List<T>,
        distinct: (oldItem: T, newItem: T) -> Boolean,
    ): Boolean {
        return modifyList {
            it.addAllDistinctInput(
                list = list,
                distinct = distinct,
            )
        }
    }

    override suspend fun replaceFirst(block: (T) -> T): Boolean {
        return modifyList {
            it.replaceFirst(block)
        }
    }

    override suspend fun replaceAll(block: (T) -> T): Boolean {
        return modifyList {
            it.replaceAll(block)
        }
    }

    override suspend fun removeFirst(predicate: (T) -> Boolean): Boolean {
        return modifyList {
            it.removeFirst(predicate)
        }
    }

    override suspend fun removeAll(predicate: (T) -> Boolean): Boolean {
        return modifyList {
            it.removeAll(predicate)
        }
    }

    override suspend fun modify(block: (list: MutableList<T>) -> Boolean): Boolean {
        return modifyList { list ->
            val mutableList = list.data.toMutableList()
            val oldSize = mutableList.size
            block(mutableList) || mutableList.size != oldSize
        }
    }

    private suspend fun modifyList(block: (list: FList<T>) -> Boolean): Boolean {
        return dispatch {
            val oldSize = _list.data.size
            if (block(_list) || _list.data.size != oldSize) {
                _dataFlow.value = _list.data.toList()
                true
            } else {
                false
            }
        }
    }

    private suspend fun <T> dispatch(block: suspend CoroutineScope.() -> T): T {
        return withContext(dispatcher, block)
    }
}