package com.sd.demo.list

import com.sd.lib.list.FList
import org.junit.Assert.assertEquals
import org.junit.Test

class FListTest {
    @Test
    fun `test set clear`() {
        val list = FList<Int>()

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
    fun `test add`() {
        val list = FList<Int>()

        list.add(1)
        assertEquals(listOf(1), list.data)

        list.add(2)
        assertEquals(listOf(1, 2), list.data)
    }

    @Test
    fun `test addAll`() {
        val list = FList<Int>()
        list.addAll(listOf(1, 2, 3))
        list.addAll(listOf(1, 2, 4))
        assertEquals(listOf(3, 1, 2, 4), list.data)
    }

    @Test
    fun `test addAllDistinctInput`() {
        val list = FList<Int>()
        list.addAll(listOf(1, 2, 3))
        list.addAllDistinctInput(listOf(1, 2, 4))
        assertEquals(listOf(1, 2, 3, 4), list.data)
    }

    @Test
    fun `test replaceFirst`() {
        val list = FList<Int>().apply {
            addAll(listOf(1, 1, 1))
        }

        list.replaceFirst { if (it == 1) 0 else it }
        assertEquals(listOf(0, 1, 1), list.data)

        list.replaceFirst { if (it == 1) 0 else it }
        assertEquals(listOf(0, 0, 1), list.data)
    }

    @Test
    fun `test replaceAll`() {
        val list = FList<Int>().apply {
            addAll(listOf(1, 1, 1))
        }

        list.replaceAll { if (it == 1) 0 else it }
        assertEquals(listOf(0, 0, 0), list.data)
    }

    @Test
    fun `test removeFirst`() {
        val list = FList<Int>().apply {
            addAll(listOf(1, 1, 1))
        }

        list.removeFirst { it == 1 }
        assertEquals(listOf(1, 1), list.data)
    }

    @Test
    fun `test removeAll`() {
        val list = FList<Int>().apply {
            addAll(listOf(1, 1, 1))
        }

        list.removeAll { it == 1 }
        assertEquals(emptyList<Int>(), list.data)
    }
}