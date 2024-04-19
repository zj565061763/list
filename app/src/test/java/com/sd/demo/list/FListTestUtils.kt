package com.sd.demo.list

import com.sd.lib.list.FList
import org.junit.Assert.assertEquals

object FListTestUtils {
    fun `test set clear`(list: FList<Int>) {
        assertEquals(true, list.data.isEmpty())

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

    fun `test add`(list: FList<Int>) {
        assertEquals(true, list.data.isEmpty())

        list.add(1)
        assertEquals(listOf(1), list.data)

        list.add(2)
        assertEquals(listOf(1, 2), list.data)
    }

    fun `test addAll`(list: FList<Int>) {
        assertEquals(true, list.data.isEmpty())
        list.addAll(listOf(1, 2, 3))
        list.addAll(listOf(1, 2, 4))
        assertEquals(listOf(3, 1, 2, 4), list.data)
    }

    fun `test addAllDistinctInput`(list: FList<Int>) {
        assertEquals(true, list.data.isEmpty())
        list.addAll(listOf(1, 2, 3))
        list.addAllDistinctInput(listOf(1, 2, 4))
        assertEquals(listOf(1, 2, 3, 4), list.data)
    }

    fun `test replaceFirst`(list: FList<Int>) {
        assertEquals(true, list.data.isEmpty())
        list.addAll(listOf(1, 1, 1))

        list.replaceFirst { if (it == 1) 0 else it }
        assertEquals(listOf(0, 1, 1), list.data)

        list.replaceFirst { if (it == 1) 0 else it }
        assertEquals(listOf(0, 0, 1), list.data)
    }

    fun `test replaceAll`(list: FList<Int>) {
        assertEquals(true, list.data.isEmpty())
        list.addAll(listOf(1, 1, 1))
        list.replaceAll { if (it == 1) 0 else it }
        assertEquals(listOf(0, 0, 0), list.data)
    }

    fun `test removeFirst`(list: FList<Int>) {
        assertEquals(true, list.data.isEmpty())
        list.addAll(listOf(1, 1, 1))
        list.removeFirst { it == 1 }
        assertEquals(listOf(1, 1), list.data)
    }

    fun `test removeAll`(list: FList<Int>) {
        assertEquals(true, list.data.isEmpty())
        list.addAll(listOf(1, 1, 1))
        list.removeAll { it == 1 }
        assertEquals(emptyList<Int>(), list.data)
    }
}