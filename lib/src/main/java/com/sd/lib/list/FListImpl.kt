package com.sd.lib.list

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

internal class FListImpl<T>(
    initial: List<T>,
    private val dispatcher: CoroutineDispatcher,
) : FList<T> {

    private val _list: MutableList<T> = mutableListOf<T>().apply { this.addAll(initial) }
    private val _dataFlow: MutableStateFlow<List<T>> = MutableStateFlow(initial)

    override val data: List<T> get() = _dataFlow.value
    override val dataFlow: Flow<List<T>> = _dataFlow.asStateFlow()

    override suspend fun set(list: List<T>) {
        modify { listData ->
            listData.clear()
            listData.addAll(list)
            true
        }
    }

    override suspend fun clear() {
        modify { listData ->
            listData.clear()
            true
        }
    }

    override suspend fun add(data: T) {
        modify { listData ->
            listData.add(data)
        }
    }

    override suspend fun addAll(list: List<T>) {
        if (list.isEmpty()) return
        modify { listData ->
            listData.addAll(list)
        }
    }

    override suspend fun addAllDistinct(
        list: List<T>,
        distinct: (oldItem: T, newItem: T) -> Boolean,
    ) {
        if (list.isEmpty()) return
        modify { listData ->
            listData.removeAll { oldItem ->
                list.firstOrNull { newItem ->
                    distinct(oldItem, newItem)
                } != null
            }
            listData.addAll(list)
        }
    }

    override suspend fun addAllDistinctInput(
        list: List<T>,
        distinct: (oldItem: T, newItem: T) -> Boolean,
    ) {
        if (list.isEmpty()) return
        modify { listData ->
            val mutableList = list.toMutableList()
            mutableList.removeAll { newItem ->
                listData.firstOrNull { oldItem ->
                    distinct(oldItem, newItem)
                } != null
            }
            listData.addAll(mutableList)
        }
    }

    override suspend fun replaceFirst(block: (T) -> T) {
        modify { listData ->
            var result = false
            for (index in listData.indices) {
                val item = listData[index]
                val newItem = block(item)
                if (newItem != item) {
                    listData[index] = newItem
                    result = true
                    break
                }
            }
            result
        }
    }

    override suspend fun replaceAll(block: (T) -> T) {
        modify { listData ->
            var result = false
            for (index in listData.indices) {
                val item = listData[index]
                val newItem = block(item)
                if (newItem != item) {
                    listData[index] = newItem
                    result = true
                }
            }
            result
        }
    }

    override suspend fun removeFirst(predicate: (T) -> Boolean) {
        modify { listData ->
            listData.removeFirst(predicate)
        }
    }

    override suspend fun removeAll(predicate: (T) -> Boolean) {
        modify { listData ->
            listData.removeAll(predicate)
        }
    }

    override suspend fun modify(block: (list: MutableList<T>) -> Boolean) {
        withContext(dispatcher) {
            val oldSize = _list.size
            if (block(_list) || oldSize != _list.size) {
                ModifyResult(list = _list.toList(), oldSize = oldSize)
            } else {
                null
            }
        }?.also { result ->
            _dataFlow.value = result.list
        }
    }
}

private data class ModifyResult<T>(
    val list: List<T>,
    val oldSize: Int,
)

/**
 * 根据条件移除元素
 */
private fun <T> MutableList<T>.removeFirst(
    predicate: (T) -> Boolean,
): Boolean {
    with(iterator()) {
        while (hasNext()) {
            if (predicate(next())) {
                remove()
                return true
            }
        }
    }
    return false
}