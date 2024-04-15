package com.sd.demo.list

import app.cash.turbine.test
import com.sd.lib.list.FSuspendList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SuspendListTest {
    @Test
    fun `test set clear`() = runTest {
        val list = FSuspendList<Int>(dispatcher = currentCoroutineDispatcher())
        list.dataFlow.test {
            emptyList<Int>().let { data ->
                assertEquals(data, list.data)
                assertEquals(data, awaitItem())
            }

            listOf(0, 1, 2).let { data ->
                list.set(data)
                assertEquals(data, list.data)
                assertEquals(data, awaitItem())
            }

            emptyList<Int>().let { data ->
                list.clear()
                assertEquals(data, list.data)
                assertEquals(data, awaitItem())
            }
        }
    }

    @Test
    fun `test add`() = runTest {
        val list = FSuspendList<Int>(dispatcher = currentCoroutineDispatcher())
        list.dataFlow.test {
            emptyList<Int>().let { data ->
                assertEquals(data, list.data)
                assertEquals(data, awaitItem())
            }

            listOf(1).let { data ->
                list.add(1)
                assertEquals(data, list.data)
                assertEquals(data, awaitItem())
            }
        }
    }

    @Test
    fun `test addAll`() = runTest {
        val list = FSuspendList<Int>(dispatcher = currentCoroutineDispatcher())
        list.dataFlow.test {
            emptyList<Int>().let { data ->
                assertEquals(data, list.data)
                assertEquals(data, awaitItem())
            }

            listOf(0, 1, 2).let { data ->
                list.addAll(data)
                assertEquals(data, list.data)
                assertEquals(data, awaitItem())
            }
        }
    }

    @Test
    fun `test addAllDistinct`() = runTest {
        val list = FSuspendList(
            initial = listOf(0, 1, 2),
            dispatcher = currentCoroutineDispatcher(),
        )

        list.dataFlow.test {
            listOf(0, 1, 2).let { data ->
                assertEquals(data, list.data)
                assertEquals(data, awaitItem())
            }

            listOf(0, 2, 3, 1, 5).let { data ->
                list.addAllDistinct(listOf(3, 1, 5))
                assertEquals(data, list.data)
                assertEquals(data, awaitItem())
            }
        }
    }

    @Test
    fun `test addAllDistinctInput`() = runTest {
        val list = FSuspendList(
            initial = listOf(0, 1, 2),
            dispatcher = currentCoroutineDispatcher(),
        )

        list.dataFlow.test {
            listOf(0, 1, 2).let { data ->
                assertEquals(data, list.data)
                assertEquals(data, awaitItem())
            }

            listOf(0, 1, 2, 3, 5).let { data ->
                list.addAllDistinctInput(listOf(3, 1, 5))
                assertEquals(data, list.data)
                assertEquals(data, awaitItem())
            }
        }
    }

    @Test
    fun `test replaceFirst`() = runTest {
        val list = FSuspendList(
            initial = listOf(1, 1, 1),
            dispatcher = currentCoroutineDispatcher(),
        )

        list.dataFlow.test {
            listOf(1, 1, 1).let { data ->
                assertEquals(data, list.data)
                assertEquals(data, awaitItem())
            }

            listOf(0, 1, 1).let { data ->
                list.replaceFirst { if (it == 1) 0 else it }
                assertEquals(data, list.data)
                assertEquals(data, awaitItem())
            }
        }
    }

    @Test
    fun `test replaceAll`() = runTest {
        val list = FSuspendList(
            initial = listOf(1, 1, 1),
            dispatcher = currentCoroutineDispatcher(),
        )

        list.dataFlow.test {
            listOf(1, 1, 1).let { data ->
                assertEquals(data, list.data)
                assertEquals(data, awaitItem())
            }

            listOf(0, 0, 0).let { data ->
                list.replaceAll { if (it == 1) 0 else it }
                assertEquals(data, list.data)
                assertEquals(data, awaitItem())
            }
        }
    }

    @Test
    fun `test removeFirst`() = runTest {
        val list = FSuspendList(
            initial = listOf(1, 1, 1),
            dispatcher = currentCoroutineDispatcher(),
        )

        list.dataFlow.test {
            listOf(1, 1, 1).let { data ->
                assertEquals(data, list.data)
                assertEquals(data, awaitItem())
            }

            listOf(1, 1).let { data ->
                list.removeFirst { it == 1 }
                assertEquals(data, list.data)
                assertEquals(data, awaitItem())
            }
        }
    }

    @Test
    fun `test removeAll`() = runTest {
        val list = FSuspendList(
            initial = listOf(1, 1, 1),
            dispatcher = currentCoroutineDispatcher(),
        )

        list.dataFlow.test {
            listOf(1, 1, 1).let { data ->
                assertEquals(data, list.data)
                assertEquals(data, awaitItem())
            }

            emptyList<Int>().let { data ->
                list.removeAll { it == 1 }
                assertEquals(data, list.data)
                assertEquals(data, awaitItem())
            }
        }
    }
}

@OptIn(ExperimentalStdlibApi::class)
private suspend fun currentCoroutineDispatcher(): CoroutineDispatcher {
    return currentCoroutineContext()[CoroutineDispatcher]!!
}