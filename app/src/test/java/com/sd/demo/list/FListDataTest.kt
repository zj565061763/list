package com.sd.demo.list

import com.sd.lib.list.FList
import com.sd.lib.list.FRawList
import com.sd.lib.list.FSuspendList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class FListDataTest {

    @Test
    fun `test FRawList`() {
        val list = FRawList<Int>()

        val data1 = list.data
        list.add(1)
        val data2 = list.data

        assertEquals(true, data1 === data2)
    }

    @Test
    fun `test FList`() {
        val list = FList<Int>()

        val data1 = list.data
        list.add(1)
        val data2 = list.data

        assertEquals(false, data1 === data2)
    }

    @Test
    fun `test FSuspendList`(): Unit = runBlocking {
        val list = FSuspendList<Int>()

        val data1 = list.data
        list.add(1)
        val data2 = list.data

        assertEquals(false, data1 === data2)
    }
}