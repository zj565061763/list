package com.sd.demo.list

import com.sd.lib.list.FList
import com.sd.lib.list.FRawList
import org.junit.Assert.assertEquals
import org.junit.Test

class FListDataTest {

  @Test
  fun `test FRawList`() {
    val list = FRawList<Int>()

    list.add(1)
    val data1 = list.getData()

    list.add(1)
    val data2 = list.getData()

    assertEquals(true, data1 === data2)
  }

  @Test
  fun `test FList`() {
    val list = FList<Int>()

    list.add(1)
    val data1 = list.getData()

    list.add(1)
    val data2 = list.getData()

    assertEquals(false, data1 === data2)
  }
}