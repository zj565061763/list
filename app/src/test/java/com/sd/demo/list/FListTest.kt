package com.sd.demo.list

import com.sd.lib.list.FList
import com.sd.lib.list.FRawList
import org.junit.Assert.assertEquals
import org.junit.Test

class FListTest {
    @Test
    fun `test set`() {
        TestUtils.`test set`(FList())
        TestUtils.`test set`(FRawList())
    }

    @Test
    fun `test clear`() {
        TestUtils.`test clear`(FList())
        TestUtils.`test clear`(FRawList())
    }

    @Test
    fun `test add`() {
        TestUtils.`test add`(FList())
        TestUtils.`test add`(FRawList())
    }

    @Test
    fun `test addAll`() {
        TestUtils.`test addAll`(FList())
        TestUtils.`test addAll`(FRawList())
    }

    @Test
    fun `test addAll none distinct`() {
        TestUtils.`test addAll none distinct`(FList())
        TestUtils.`test addAll none distinct`(FRawList())
    }

    @Test
    fun `test addAllDistinctInput`() {
        TestUtils.`test addAllDistinctInput`(FList())
        TestUtils.`test addAllDistinctInput`(FRawList())
    }

    @Test
    fun `test addAllDistinctInput none distinct`() {
        TestUtils.`test addAllDistinctInput  none distinct`(FList())
        TestUtils.`test addAllDistinctInput  none distinct`(FRawList())
    }

    @Test
    fun `test replaceFirst`() {
        TestUtils.`test replaceFirst`(FList())
        TestUtils.`test replaceFirst`(FRawList())
    }

    @Test
    fun `test replaceAll`() {
        TestUtils.`test replaceAll`(FList())
        TestUtils.`test replaceAll`(FRawList())
    }

    @Test
    fun `test removeFirst`() {
        TestUtils.`test removeFirst`(FList())
        TestUtils.`test removeFirst`(FRawList())
    }

    @Test
    fun `test removeAll`() {
        TestUtils.`test removeAll`(FList())
        TestUtils.`test removeAll`(FRawList())
    }

    @Test
    fun `test insert`() {
        TestUtils.`test insert`(FList())
        TestUtils.`test insert`(FRawList())
    }

    @Test
    fun `test insertAll`() {
        TestUtils.`test insertAll`(FList())
        TestUtils.`test insertAll`(FRawList())
    }
}

private object TestUtils {
    fun `test set`(list: FList<Int>) {
        assertEquals(true, list.data.isEmpty())

        listOf(1, 2, 3).let { data ->
            list.set(data).also { assertEquals(true, it) }
            assertEquals(data, list.data)
        }

        listOf(4, 5, 6).let { data ->
            list.set(data).also { assertEquals(true, it) }
            assertEquals(data, list.data)
        }

        emptyList<Int>().let { data ->
            list.set(data).also { assertEquals(true, it) }
            list.set(data).also { assertEquals(false, it) }
            assertEquals(data, list.data)
        }
    }

    fun `test clear`(list: FList<Int>) {
        assertEquals(true, list.data.isEmpty())

        list.set(listOf(1, 2, 3))
        list.clear().also { assertEquals(true, it) }
        list.clear().also { assertEquals(false, it) }

        assertEquals(emptyList<Int>(), list.data)
    }

    fun `test add`(list: FList<Int>) {
        assertEquals(true, list.data.isEmpty())

        list.add(1).also { assertEquals(true, it) }
        assertEquals(listOf(1), list.data)

        list.add(2).also { assertEquals(true, it) }
        assertEquals(listOf(1, 2), list.data)
    }

    fun `test addAll`(list: FList<Int>) {
        assertEquals(true, list.data.isEmpty())
        list.addAll(listOf(1, 2, 3)).also { assertEquals(true, it) }
        list.addAll(listOf(1, 2, 4)).also { assertEquals(true, it) }
        assertEquals(listOf(3, 1, 2, 4), list.data)
    }

    fun `test addAll none distinct`(list: FList<Int>) {
        assertEquals(true, list.data.isEmpty())
        list.addAll(listOf(1, 2, 3)).also { assertEquals(true, it) }
        list.addAll(listOf(1, 2, 4), distinct = null).also { assertEquals(true, it) }
        assertEquals(listOf(1, 2, 3, 1, 2, 4), list.data)
    }

    fun `test addAllDistinctInput`(list: FList<Int>) {
        assertEquals(true, list.data.isEmpty())
        list.addAll(listOf(1, 2, 3)).also { assertEquals(true, it) }
        list.addAllDistinctInput(listOf(1, 2, 4)).also { assertEquals(true, it) }
        assertEquals(listOf(1, 2, 3, 4), list.data)
    }

    fun `test addAllDistinctInput  none distinct`(list: FList<Int>) {
        assertEquals(true, list.data.isEmpty())
        list.addAll(listOf(1, 2, 3)).also { assertEquals(true, it) }
        list.addAllDistinctInput(listOf(1, 2, 4), distinct = null).also { assertEquals(true, it) }
        assertEquals(listOf(1, 2, 3, 1, 2, 4), list.data)
    }

    fun `test replaceFirst`(list: FList<Int>) {
        assertEquals(true, list.data.isEmpty())
        list.addAll(listOf(1, 1, 1)).also { assertEquals(true, it) }

        list.replaceFirst { if (it == 1) 0 else it }.also { assertEquals(true, it) }
        assertEquals(listOf(0, 1, 1), list.data)

        list.replaceFirst { if (it == 1) 0 else it }.also { assertEquals(true, it) }
        assertEquals(listOf(0, 0, 1), list.data)
    }

    fun `test replaceAll`(list: FList<Int>) {
        assertEquals(true, list.data.isEmpty())
        list.addAll(listOf(1, 1, 1)).also { assertEquals(true, it) }
        list.replaceAll { if (it == 1) 0 else it }.also { assertEquals(true, it) }
        assertEquals(listOf(0, 0, 0), list.data)
    }

    fun `test removeFirst`(list: FList<Int>) {
        assertEquals(true, list.data.isEmpty())
        list.addAll(listOf(1, 1, 1)).also { assertEquals(true, it) }

        list.removeFirst { it == 1 }.also { assertEquals(true, it) }
        assertEquals(listOf(1, 1), list.data)

        list.removeFirst { it == 1 }.also { assertEquals(true, it) }
        assertEquals(listOf(1), list.data)
    }

    fun `test removeAll`(list: FList<Int>) {
        assertEquals(true, list.data.isEmpty())
        list.addAll(listOf(1, 1, 1)).also { assertEquals(true, it) }
        list.removeAll { it == 1 }.also { assertEquals(true, it) }
        assertEquals(emptyList<Int>(), list.data)
    }

    fun `test insert`(list: FList<Int>) {
        assertEquals(true, list.data.isEmpty())
        list.insert(0, 3).also { assertEquals(true, it) }
        list.insert(0, 2).also { assertEquals(true, it) }
        list.insert(0, 1).also { assertEquals(true, it) }
        assertEquals(listOf(1, 2, 3), list.data)

        list.insert(1, 0)
        assertEquals(listOf(1, 0, 2, 3), list.data)
    }

    fun `test insertAll`(list: FList<Int>) {
        assertEquals(true, list.data.isEmpty())
        list.addAll(listOf(1, 2, 3)).also { assertEquals(true, it) }
        list.insertAll(0, listOf(1, 2, 4)).also { assertEquals(true, it) }
        assertEquals(listOf(1, 2, 4, 3), list.data)
    }
}