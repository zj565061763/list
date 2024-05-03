package com.sd.demo.list

import com.sd.lib.list.FList
import org.junit.Assert.assertEquals
import org.junit.Test

class FListTest {
    @Test
    fun `test set`() {
        val list = FList<Int>()

        listOf(1, 2, 3).let { data ->
            list.set(data).also {
                assertEquals(true, it)
                assertEquals(data, list.data)
            }
        }

        listOf(4, 5, 6).let { data ->
            list.set(data).also {
                assertEquals(true, it)
                assertEquals(data, list.data)
            }
        }

        emptyList<Int>().let { data ->
            list.set(data).also {
                assertEquals(true, it)
                assertEquals(data, list.data)
            }
            list.set(data).also {
                assertEquals(false, it)
                assertEquals(data, list.data)
            }
        }
    }

    @Test
    fun `test clear`() {
        val list = FList<Int>()

        list.set(listOf(1, 2, 3)).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2, 3), list.data)
        }

        list.clear().also {
            assertEquals(true, it)
            assertEquals(emptyList<Int>(), list.data)
        }

        list.clear().also {
            assertEquals(false, it)
            assertEquals(emptyList<Int>(), list.data)
        }
    }

    @Test
    fun `test add`() {
        val list = FList<Int>()

        list.add(1).also {
            assertEquals(true, it)
            assertEquals(listOf(1), list.data)
        }

        list.add(2).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2), list.data)
        }
    }

    @Test
    fun `test addAll`() {
        val list = FList<Int>()

        list.addAll(listOf(1, 2, 3)).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2, 3), list.data)
        }

        list.addAll(listOf(1, 2, 4)).also {
            assertEquals(true, it)
            assertEquals(listOf(3, 1, 2, 4), list.data)
        }

        list.addAll(emptyList()).also {
            assertEquals(false, it)
            assertEquals(listOf(3, 1, 2, 4), list.data)
        }
    }

    @Test
    fun `test addAll none distinct`() {
        val list = FList<Int>()

        list.addAll(listOf(1, 2, 3)).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2, 3), list.data)
        }

        list.addAll(listOf(1, 2, 4), distinct = null).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2, 3, 1, 2, 4), list.data)
        }

        list.addAll(emptyList(), distinct = null).also {
            assertEquals(false, it)
            assertEquals(listOf(1, 2, 3, 1, 2, 4), list.data)
        }
    }

    @Test
    fun `test addAllDistinctInput`() {
        val list = FList<Int>()

        list.addAll(listOf(1, 2, 3)).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2, 3), list.data)
        }

        list.addAllDistinctInput(listOf(1, 2, 4)).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2, 3, 4), list.data)
        }

        list.addAllDistinctInput(emptyList()).also {
            assertEquals(false, it)
            assertEquals(listOf(1, 2, 3, 4), list.data)
        }
    }

    @Test
    fun `test addAllDistinctInput none distinct`() {
        val list = FList<Int>()

        list.addAll(listOf(1, 2, 3)).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2, 3), list.data)
        }

        list.addAllDistinctInput(listOf(1, 2, 4), distinct = null).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2, 3, 1, 2, 4), list.data)
        }

        list.addAllDistinctInput(emptyList(), distinct = null).also {
            assertEquals(false, it)
            assertEquals(listOf(1, 2, 3, 1, 2, 4), list.data)
        }
    }

    @Test
    fun `test replaceFirst`() {
        val list = FList<Int>()

        list.addAll(listOf(1, 1, 1)).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 1, 1), list.data)
        }

        list.replaceFirst { if (it == 1) 0 else it }.also {
            assertEquals(true, it)
            assertEquals(listOf(0, 1, 1), list.data)
        }

        list.replaceFirst { if (it == 1) 0 else it }.also {
            assertEquals(true, it)
            assertEquals(listOf(0, 0, 1), list.data)
        }

        list.replaceFirst { if (it == 100) 0 else it }.also {
            assertEquals(false, it)
            assertEquals(listOf(0, 0, 1), list.data)
        }
    }

    @Test
    fun `test replaceAll`() {
        val list = FList<Int>()

        list.addAll(listOf(1, 1, 1)).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 1, 1), list.data)
        }

        list.replaceAll { if (it == 1) 0 else it }.also {
            assertEquals(true, it)
            assertEquals(listOf(0, 0, 0), list.data)
        }

        list.replaceAll { if (it == 100) 0 else it }.also {
            assertEquals(false, it)
            assertEquals(listOf(0, 0, 0), list.data)
        }
    }

    @Test
    fun `test removeFirst`() {
        val list = FList<Int>()

        list.addAll(listOf(1, 1, 1)).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 1, 1), list.data)
        }

        list.removeFirst { it == 1 }.also {
            assertEquals(true, it)
            assertEquals(listOf(1, 1), list.data)
        }

        list.removeFirst { it == 1 }.also {
            assertEquals(true, it)
            assertEquals(listOf(1), list.data)
        }

        list.removeFirst { it == 100 }.also {
            assertEquals(false, it)
            assertEquals(listOf(1), list.data)
        }
    }

    @Test
    fun `test removeAll`() {
        val list = FList<Int>()

        list.addAll(listOf(1, 1, 1)).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 1, 1), list.data)
        }

        list.removeAll { it == 100 }.also {
            assertEquals(false, it)
            assertEquals(listOf(1, 1, 1), list.data)
        }

        list.removeAll { it == 1 }.also {
            assertEquals(true, it)
            assertEquals(emptyList<Int>(), list.data)
        }
    }

    @Test
    fun `test insert`() {
        val list = FList<Int>()

        list.insert(0, 3).also {
            assertEquals(true, it)
            assertEquals(listOf(3), list.data)
        }

        list.insert(0, 2).also {
            assertEquals(true, it)
            assertEquals(listOf(2, 3), list.data)
        }

        list.insert(0, 1).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2, 3), list.data)
        }

        list.insert(1, 0).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 0, 2, 3), list.data)
        }
    }

    @Test
    fun `test insertAll`() {
        val list = FList<Int>()

        list.addAll(listOf(1, 2, 3)).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2, 3), list.data)
        }

        list.insertAll(0, listOf(1, 2, 4)).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2, 4, 3), list.data)
        }

        list.insertAll(0, emptyList()).also {
            assertEquals(false, it)
            assertEquals(listOf(1, 2, 4, 3), list.data)
        }
    }

    @Test
    fun `test insertAll none distinct`() {
        val list = FList<Int>()

        list.addAll(listOf(1, 2, 3)).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2, 3), list.data)
        }

        list.insertAll(0, listOf(1, 2, 4), distinct = null).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2, 4, 1, 2, 3), list.data)
        }

        list.insertAll(0, emptyList(), distinct = null).also {
            assertEquals(false, it)
            assertEquals(listOf(1, 2, 4, 1, 2, 3), list.data)
        }
    }

    @Test
    fun `test insertAllDistinctInput`() {
        val list = FList<Int>()

        list.addAll(listOf(1, 2, 3)).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2, 3), list.data)
        }

        list.insertAllDistinctInput(0, listOf(1, 2, 4)).also {
            assertEquals(true, it)
            assertEquals(listOf(4, 1, 2, 3), list.data)
        }

        list.insertAllDistinctInput(0, emptyList()).also {
            assertEquals(false, it)
            assertEquals(listOf(4, 1, 2, 3), list.data)
        }
    }

    @Test
    fun `test insertAllDistinctInput none distinct`() {
        val list = FList<Int>()

        list.addAll(listOf(1, 2, 3)).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2, 3), list.data)
        }

        list.insertAllDistinctInput(0, listOf(1, 2, 4), distinct = null).also {
            assertEquals(true, it)
            assertEquals(listOf(1, 2, 4, 1, 2, 3), list.data)
        }

        list.insertAllDistinctInput(0, emptyList(), distinct = null).also {
            assertEquals(false, it)
            assertEquals(listOf(1, 2, 4, 1, 2, 3), list.data)
        }
    }
}