package com.sd.demo.list

import com.sd.lib.list.FList
import com.sd.lib.list.synchronizedList
import org.junit.Assert.assertEquals
import org.junit.Test

class FListTest {

  private val _list: List<FList<Int>>
    get() = listOf(
      FList(),
      FList<Int>().synchronizedList(),
    )

  @Test
  fun `test set`() {
    _list.forEach {
      TestUtils.`test set`(it)
    }
  }

  @Test
  fun `test clear`() {
    _list.forEach {
      TestUtils.`test clear`(it)
    }
  }

  @Test
  fun `test add`() {
    _list.forEach {
      TestUtils.`test add`(it)
    }
  }

  @Test
  fun `test addAll`() {
    _list.forEach {
      TestUtils.`test addAll`(it)
    }
  }

  @Test
  fun `test addAll none distinct`() {
    _list.forEach {
      TestUtils.`test addAll none distinct`(it)
    }
  }

  @Test
  fun `test addAllDistinctInput`() {
    _list.forEach {
      TestUtils.`test addAllDistinctInput`(it)
    }
  }

  @Test
  fun `test addAllDistinctInput none distinct`() {
    _list.forEach {
      TestUtils.`test addAllDistinctInput none distinct`(it)
    }
  }

  @Test
  fun `test replaceFirst`() {
    _list.forEach {
      TestUtils.`test replaceFirst`(it)
    }
  }

  @Test
  fun `test replaceAll`() {
    _list.forEach {
      TestUtils.`test replaceAll`(it)
    }
  }

  @Test
  fun `test replaceAt`() {
    _list.forEach {
      TestUtils.`test replaceAt`(it)
    }
  }

  @Test
  fun `test removeFirst`() {
    _list.forEach {
      TestUtils.`test removeFirst`(it)
    }
  }

  @Test
  fun `test removeAll`() {
    _list.forEach {
      TestUtils.`test removeAll`(it)
    }
  }

  @Test
  fun `test removeAt`() {
    _list.forEach {
      TestUtils.`test removeAt`(it)
    }
  }

  @Test
  fun `test insert`() {
    _list.forEach {
      TestUtils.`test insert`(it)
    }
  }

  @Test
  fun `test insertAll`() {
    _list.forEach {
      TestUtils.`test insertAll`(it)
    }
  }

  @Test
  fun `test insertAll none distinct`() {
    _list.forEach {
      TestUtils.`test insertAll none distinct`(it)
    }
  }

  @Test
  fun `test insertAllDistinctInput`() {
    _list.forEach {
      TestUtils.`test insertAllDistinctInput`(it)
    }
  }

  @Test
  fun `test insertAllDistinctInput none distinct`() {
    _list.forEach {
      TestUtils.`test insertAllDistinctInput none distinct`(it)
    }
  }

  @Test
  fun `test modify`() {
    _list.forEach {
      TestUtils.`test modify`(it)
    }
  }
}

private object TestUtils {
  fun `test set`(list: FList<Int>) {
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

  fun `test clear`(list: FList<Int>) {
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

  fun `test add`(list: FList<Int>) {
    list.add(1).also {
      assertEquals(true, it)
      assertEquals(listOf(1), list.getData())
    }

    list.add(2).also {
      assertEquals(true, it)
      assertEquals(listOf(1, 2), list.getData())
    }
  }

  fun `test addAll`(list: FList<Int>) {
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

  fun `test addAll none distinct`(list: FList<Int>) {
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

  fun `test addAllDistinctInput`(list: FList<Int>) {
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

  fun `test addAllDistinctInput none distinct`(list: FList<Int>) {
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

  fun `test replaceFirst`(list: FList<Int>) {
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

  fun `test replaceAll`(list: FList<Int>) {
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

  fun `test replaceAt`(list: FList<Int>) {
    list.addAll(listOf(1, 2, 3)).also {
      assertEquals(true, it)
      assertEquals(listOf(1, 2, 3), list.getData())
    }

    list.replaceAt(1, 100).also {
      assertEquals(true, it)
      assertEquals(listOf(1, 100, 3), list.getData())
    }
  }

  fun `test removeFirst`(list: FList<Int>) {
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

  fun `test removeAll`(list: FList<Int>) {
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

  fun `test removeAt`(list: FList<Int>) {
    list.addAll(listOf(1, 2, 3)).also {
      assertEquals(true, it)
      assertEquals(listOf(1, 2, 3), list.getData())
    }

    list.removeAt(1).also {
      assertEquals(true, it)
      assertEquals(listOf(1, 3), list.getData())
    }
  }


  fun `test insert`(list: FList<Int>) {
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

  fun `test insertAll`(list: FList<Int>) {
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

  fun `test insertAll none distinct`(list: FList<Int>) {
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

  fun `test insertAllDistinctInput`(list: FList<Int>) {
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

  fun `test insertAllDistinctInput none distinct`(list: FList<Int>) {
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

  fun `test modify`(list: FList<Int>) {
    list.modify {
      add(1)
      add(2)
      add(3)
    }
    assertEquals(listOf(1, 2, 3), list.getData())
  }
}