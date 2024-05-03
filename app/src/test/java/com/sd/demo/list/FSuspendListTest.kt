package com.sd.demo.list

import com.sd.lib.list.FSuspendList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class FSuspendListTest {
    @Test
    fun `test set`(): Unit = runBlocking {
        val list = FSuspendList<Int>()

        listOf(1, 2, 3).let { data ->
            list.set(data).also {
                assertEquals(true, it)
                assertEquals(data, list.getData())
            }
        }

        listOf(4, 5, 6).let { data ->
            list.set(data).also {
                assertEquals(true, it)
                assertEquals(data, list.getData())
            }
        }

        emptyList<Int>().let { data ->
            list.set(data).also {
                assertEquals(true, it)
                assertEquals(data, list.getData())
            }
            list.set(data).also {
                assertEquals(false, it)
                assertEquals(data, list.getData())
            }
        }
    }

    @Test
    fun `test clear`(): Unit = runBlocking {
        val list = FSuspendList<Int>()

        list.set(listOf(1, 2, 3)).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2, 3), list.getData())
        }

        list.clear().also {
            assertEquals(true, it)
            assertEquals(emptyList<Int>(), list.getData())
        }

        list.clear().also {
            assertEquals(false, it)
            assertEquals(emptyList<Int>(), list.getData())
        }
    }

    @Test
    fun `test add`(): Unit = runBlocking {
        val list = FSuspendList<Int>()

        list.add(1).also {
            assertEquals(true, it)
            assertEquals(listOf(1), list.getData())
        }

        list.add(2).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2), list.getData())
        }
    }

    @Test
    fun `test addAll`(): Unit = runBlocking {
        val list = FSuspendList<Int>()

        list.addAll(listOf(1, 2, 3)).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2, 3), list.getData())
        }

        list.addAll(listOf(1, 2, 4)).also {
            assertEquals(true, it)
            assertEquals(listOf(3, 1, 2, 4), list.getData())
        }

        list.addAll(emptyList()).also {
            assertEquals(false, it)
            assertEquals(listOf(3, 1, 2, 4), list.getData())
        }
    }

    @Test
    fun `test addAll none distinct`(): Unit = runBlocking {
        val list = FSuspendList<Int>()

        list.addAll(listOf(1, 2, 3)).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2, 3), list.getData())
        }

        list.addAll(listOf(1, 2, 4), distinct = null).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2, 3, 1, 2, 4), list.getData())
        }

        list.addAll(emptyList(), distinct = null).also {
            assertEquals(false, it)
            assertEquals(listOf(1, 2, 3, 1, 2, 4), list.getData())
        }
    }

    @Test
    fun `test addAllDistinctInput`(): Unit = runBlocking {
        val list = FSuspendList<Int>()

        list.addAll(listOf(1, 2, 3)).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2, 3), list.getData())
        }

        list.addAllDistinctInput(listOf(1, 2, 4)).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2, 3, 4), list.getData())
        }

        list.addAllDistinctInput(emptyList()).also {
            assertEquals(false, it)
            assertEquals(listOf(1, 2, 3, 4), list.getData())
        }
    }

    @Test
    fun `test addAllDistinctInput none distinct`(): Unit = runBlocking {
        val list = FSuspendList<Int>()

        list.addAll(listOf(1, 2, 3)).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2, 3), list.getData())
        }

        list.addAllDistinctInput(listOf(1, 2, 4), distinct = null).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2, 3, 1, 2, 4), list.getData())
        }

        list.addAllDistinctInput(emptyList(), distinct = null).also {
            assertEquals(false, it)
            assertEquals(listOf(1, 2, 3, 1, 2, 4), list.getData())
        }
    }

    @Test
    fun `test replaceFirst`(): Unit = runBlocking {
        val list = FSuspendList<Int>()

        list.addAll(listOf(1, 1, 1)).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 1, 1), list.getData())
        }

        list.replaceFirst { if (it == 1) 0 else it }.also {
            assertEquals(true, it)
            assertEquals(listOf(0, 1, 1), list.getData())
        }

        list.replaceFirst { if (it == 1) 0 else it }.also {
            assertEquals(true, it)
            assertEquals(listOf(0, 0, 1), list.getData())
        }

        list.replaceFirst { if (it == 100) 0 else it }.also {
            assertEquals(false, it)
            assertEquals(listOf(0, 0, 1), list.getData())
        }
    }

    @Test
    fun `test replaceAll`(): Unit = runBlocking {
        val list = FSuspendList<Int>()

        list.addAll(listOf(1, 1, 1)).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 1, 1), list.getData())
        }

        list.replaceAll { if (it == 1) 0 else it }.also {
            assertEquals(true, it)
            assertEquals(listOf(0, 0, 0), list.getData())
        }

        list.replaceAll { if (it == 100) 0 else it }.also {
            assertEquals(false, it)
            assertEquals(listOf(0, 0, 0), list.getData())
        }
    }

    @Test
    fun `test removeFirst`(): Unit = runBlocking {
        val list = FSuspendList<Int>()

        list.addAll(listOf(1, 1, 1)).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 1, 1), list.getData())
        }

        list.removeFirst { it == 1 }.also {
            assertEquals(true, it)
            assertEquals(listOf(1, 1), list.getData())
        }

        list.removeFirst { it == 1 }.also {
            assertEquals(true, it)
            assertEquals(listOf(1), list.getData())
        }

        list.removeFirst { it == 100 }.also {
            assertEquals(false, it)
            assertEquals(listOf(1), list.getData())
        }
    }

    @Test
    fun `test removeAll`(): Unit = runBlocking {
        val list = FSuspendList<Int>()

        list.addAll(listOf(1, 1, 1)).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 1, 1), list.getData())
        }

        list.removeAll { it == 100 }.also {
            assertEquals(false, it)
            assertEquals(listOf(1, 1, 1), list.getData())
        }

        list.removeAll { it == 1 }.also {
            assertEquals(true, it)
            assertEquals(emptyList<Int>(), list.getData())
        }
    }

    @Test
    fun `test insert`(): Unit = runBlocking {
        val list = FSuspendList<Int>()

        list.insert(0, 3).also {
            assertEquals(true, it)
            assertEquals(listOf(3), list.getData())
        }

        list.insert(0, 2).also {
            assertEquals(true, it)
            assertEquals(listOf(2, 3), list.getData())
        }

        list.insert(0, 1).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2, 3), list.getData())
        }

        list.insert(1, 0).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 0, 2, 3), list.getData())
        }
    }

    @Test
    fun `test insertAll`(): Unit = runBlocking {
        val list = FSuspendList<Int>()

        list.addAll(listOf(1, 2, 3)).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2, 3), list.getData())
        }

        list.insertAll(0, listOf(1, 2, 4)).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2, 4, 3), list.getData())
        }

        list.insertAll(0, emptyList()).also {
            assertEquals(false, it)
            assertEquals(listOf(1, 2, 4, 3), list.getData())
        }
    }

    @Test
    fun `test insertAll none distinct`(): Unit = runBlocking {
        val list = FSuspendList<Int>()

        list.addAll(listOf(1, 2, 3)).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2, 3), list.getData())
        }

        list.insertAll(0, listOf(1, 2, 4), distinct = null).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2, 4, 1, 2, 3), list.getData())
        }

        list.insertAll(0, emptyList(), distinct = null).also {
            assertEquals(false, it)
            assertEquals(listOf(1, 2, 4, 1, 2, 3), list.getData())
        }
    }

    @Test
    fun `test insertAllDistinctInput`(): Unit = runBlocking {
        val list = FSuspendList<Int>()

        list.addAll(listOf(1, 2, 3)).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2, 3), list.getData())
        }

        list.insertAllDistinctInput(0, listOf(1, 2, 4)).also {
            assertEquals(true, it)
            assertEquals(listOf(4, 1, 2, 3), list.getData())
        }

        list.insertAllDistinctInput(0, emptyList()).also {
            assertEquals(false, it)
            assertEquals(listOf(4, 1, 2, 3), list.getData())
        }
    }

    @Test
    fun `test insertAllDistinctInput none distinct`(): Unit = runBlocking {
        val list = FSuspendList<Int>()

        list.addAll(listOf(1, 2, 3)).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2, 3), list.getData())
        }

        list.insertAllDistinctInput(0, listOf(1, 2, 4), distinct = null).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2, 4, 1, 2, 3), list.getData())
        }

        list.insertAllDistinctInput(0, emptyList(), distinct = null).also {
            assertEquals(false, it)
            assertEquals(listOf(1, 2, 4, 1, 2, 3), list.getData())
        }
    }
}