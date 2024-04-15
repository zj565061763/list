package com.sd.demo.list

import com.sd.lib.list.FSuspendList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class FSuspendListTest {
    @Test
    fun `test set clear`() = runTest {
        val list = FSuspendList<Int>()

        // set
        listOf(1, 2, 3).let { data ->
            list.set(data)
            assertEquals(data, list.data)
        }
        listOf(4, 5, 6).let { data ->
            list.set(data)
            assertEquals(data, list.data)
        }

        // clear
        list.clear()
        assertEquals(emptyList<Int>(), list.data)
    }

    @Test
    fun `test add`() = runTest {
        val list = FSuspendList<Int>()

        list.add(1)
        assertEquals(listOf(1), list.data)

        list.add(2)
        assertEquals(listOf(1, 2), list.data)
    }

    @Test
    fun `test addAll`() = runTest {
        val list = FSuspendList<Int>()

        list.addAll(listOf(1, 2, 3), null)
        assertEquals(listOf(1, 2, 3), list.data)

        list.addAll(listOf(4, 5, 6), null)
        assertEquals(listOf(1, 2, 3, 4, 5, 6), list.data)
    }

    @Test
    fun `test addAll distinct`() = runTest {
        val list = FSuspendList<Int>().apply {
            addAll(listOf(1, 2, 3), null)
        }

        list.addAll(listOf(3, 1, 5)) { oldItem, newItem -> oldItem == newItem }
        assertEquals(listOf(2, 3, 1, 5), list.data)
    }

    @Test
    fun `test addAllDistinctInput`() = runTest {
        val list = FSuspendList<Int>().apply {
            addAll(listOf(1, 2, 3), null)
        }

        list.addAllDistinctInput(listOf(3, 1, 5))
        assertEquals(listOf(1, 2, 3, 5), list.data)
    }

    @Test
    fun `test replaceFirst`() = runTest {
        val list = FSuspendList<Int>().apply {
            addAll(listOf(1, 1, 1), null)
        }

        list.replaceFirst { if (it == 1) 0 else it }
        assertEquals(listOf(0, 1, 1), list.data)

        list.replaceFirst { if (it == 1) 0 else it }
        assertEquals(listOf(0, 0, 1), list.data)
    }

    @Test
    fun `test replaceAll`() = runTest {
        val list = FSuspendList<Int>().apply {
            addAll(listOf(1, 1, 1), null)
        }

        list.replaceAll { if (it == 1) 0 else it }
        assertEquals(listOf(0, 0, 0), list.data)
    }

    @Test
    fun `test removeFirst`() = runTest {
        val list = FSuspendList<Int>().apply {
            addAll(listOf(1, 1, 1), null)
        }

        list.removeFirst { it == 1 }
        assertEquals(listOf(1, 1), list.data)
    }

    @Test
    fun `test removeAll`() = runTest {
        val list = FSuspendList<Int>().apply {
            addAll(listOf(1, 1, 1), null)
        }

        list.removeAll { it == 1 }
        assertEquals(emptyList<Int>(), list.data)
    }
}

@OptIn(ExperimentalStdlibApi::class)
private suspend fun currentCoroutineDispatcher(): CoroutineDispatcher {
    return currentCoroutineContext()[CoroutineDispatcher]!!
}