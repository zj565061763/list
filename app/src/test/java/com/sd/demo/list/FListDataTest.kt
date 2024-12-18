package com.sd.demo.list

import com.sd.lib.list.FList
import org.junit.Assert.assertEquals
import org.junit.Test

class FListDataTest {
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