package com.sd.demo.list

import com.sd.lib.list.FSuspendList
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class FSuspendListTest {
    @Test
    fun `test set`(): Unit = runBlocking {
        val list = FSuspendList<Int>()

        listOf(1, 2, 3).let { data ->
            list.set(data).also { assertEquals(true, it) }
            assertEquals(data, list.data)
        }

        listOf(4, 5, 6).let { data ->
            list.set(data).also { assertEquals(true, it) }
            assertEquals(data, list.data)
        }

        emptyList<Int>().let { data ->
            list.set(data).also { TestCase.assertEquals(true, it) }
            list.set(data).also { TestCase.assertEquals(false, it) }
            TestCase.assertEquals(data, list.data)
        }
    }

    @Test
    fun `test clear`(): Unit = runBlocking {
        val list = FSuspendList<Int>()

        list.set(listOf(1, 2, 3))
        list.clear().also { assertEquals(true, it) }
        list.clear().also { assertEquals(false, it) }

        assertEquals(emptyList<Int>(), list.data)
    }

    @Test
    fun `test add`(): Unit = runBlocking {
        val list = FSuspendList<Int>()

        list.add(1).also { assertEquals(true, it) }
        assertEquals(listOf(1), list.data)

        list.add(2).also { assertEquals(true, it) }
        assertEquals(listOf(1, 2), list.data)
    }

    @Test
    fun `test addAll`(): Unit = runBlocking {
        val list = FSuspendList<Int>()
        list.addAll(listOf(1, 2, 3)).also { assertEquals(true, it) }
        list.addAll(listOf(1, 2, 4)).also { assertEquals(true, it) }
        assertEquals(listOf(3, 1, 2, 4), list.data)
    }

    @Test
    fun `test addAll none distinct`(): Unit = runBlocking {
        val list = FSuspendList<Int>()
        list.addAll(listOf(1, 2, 3)).also { assertEquals(true, it) }
        list.addAll(listOf(1, 2, 4), distinct = null).also { assertEquals(true, it) }
        assertEquals(listOf(1, 2, 3, 1, 2, 4), list.data)
    }

    @Test
    fun `test addAllDistinctInput`(): Unit = runBlocking {
        val list = FSuspendList<Int>()
        list.addAll(listOf(1, 2, 3)).also { assertEquals(true, it) }
        list.addAllDistinctInput(listOf(1, 2, 4)).also { assertEquals(true, it) }
        assertEquals(listOf(1, 2, 3, 4), list.data)
    }

    @Test
    fun `test addAllDistinctInput none distinct`(): Unit = runBlocking {
        val list = FSuspendList<Int>()
        list.addAll(listOf(1, 2, 3)).also { assertEquals(true, it) }
        list.addAllDistinctInput(listOf(1, 2, 4), distinct = null).also { assertEquals(true, it) }
        assertEquals(listOf(1, 2, 3, 1, 2, 4), list.data)
    }

    @Test
    fun `test replaceFirst`(): Unit = runBlocking {
        val list = FSuspendList<Int>()
        list.addAll(listOf(1, 1, 1)).also { assertEquals(true, it) }

        list.replaceFirst { if (it == 1) 0 else it }.also { assertEquals(true, it) }
        assertEquals(listOf(0, 1, 1), list.data)

        list.replaceFirst { if (it == 1) 0 else it }.also { assertEquals(true, it) }
        assertEquals(listOf(0, 0, 1), list.data)
    }

    @Test
    fun `test replaceAll`(): Unit = runBlocking {
        val list = FSuspendList<Int>()
        list.addAll(listOf(1, 1, 1)).also { assertEquals(true, it) }
        list.replaceAll { if (it == 1) 0 else it }.also { assertEquals(true, it) }
        assertEquals(listOf(0, 0, 0), list.data)
    }

    @Test
    fun `test removeFirst`(): Unit = runBlocking {
        val list = FSuspendList<Int>()
        list.addAll(listOf(1, 1, 1)).also { assertEquals(true, it) }

        list.removeFirst { it == 1 }.also { assertEquals(true, it) }
        assertEquals(listOf(1, 1), list.data)

        list.removeFirst { it == 1 }.also { assertEquals(true, it) }
        assertEquals(listOf(1), list.data)
    }

    @Test
    fun `test removeAll`(): Unit = runBlocking {
        val list = FSuspendList<Int>()
        list.addAll(listOf(1, 1, 1)).also { assertEquals(true, it) }
        list.removeAll { it == 1 }.also { assertEquals(true, it) }
        assertEquals(emptyList<Int>(), list.data)
    }

    @Test
    fun `test insert`(): Unit = runBlocking {
        val list = FSuspendList<Int>()
        list.insert(0, 3).also { assertEquals(true, it) }
        list.insert(0, 2).also { assertEquals(true, it) }
        list.insert(0, 1).also { assertEquals(true, it) }
        assertEquals(listOf(1, 2, 3), list.data)

        list.insert(1, 0)
        assertEquals(listOf(1, 0, 2, 3), list.data)
    }

    @Test
    fun `test insertAll`(): Unit = runBlocking {
        val list = FSuspendList<Int>()
        list.addAll(listOf(1, 2, 3)).also { assertEquals(true, it) }
        list.insertAll(0, listOf(1, 2, 4)).also { assertEquals(true, it) }
        assertEquals(listOf(1, 2, 4, 3), list.data)
    }

    @Test
    fun `test insertAll none distinct`(): Unit = runBlocking {
        val list = FSuspendList<Int>()
        list.addAll(listOf(1, 2, 3)).also { assertEquals(true, it) }
        list.insertAll(0, listOf(1, 2, 4), distinct = null).also { assertEquals(true, it) }
        assertEquals(listOf(1, 2, 4, 1, 2, 3), list.data)
    }
}