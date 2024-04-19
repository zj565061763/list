package com.sd.demo.list

import com.sd.lib.list.FList
import com.sd.lib.list.FRawList
import org.junit.Test

class FListTest {
    @Test
    fun `test set clear`() {
        FListTestUtils.`test set clear`(FList())
        FListTestUtils.`test set clear`(FRawList())
    }

    @Test
    fun `test add`() {
        FListTestUtils.`test add`(FList())
        FListTestUtils.`test add`(FRawList())
    }

    @Test
    fun `test addAll`() {
        FListTestUtils.`test addAll`(FList())
        FListTestUtils.`test addAll`(FRawList())
    }

    @Test
    fun `test addAllDistinctInput`() {
        FListTestUtils.`test addAllDistinctInput`(FList())
        FListTestUtils.`test addAllDistinctInput`(FRawList())
    }

    @Test
    fun `test replaceFirst`() {
        FListTestUtils.`test replaceFirst`(FList())
        FListTestUtils.`test replaceFirst`(FRawList())
    }

    @Test
    fun `test replaceAll`() {
        FListTestUtils.`test replaceAll`(FList())
        FListTestUtils.`test replaceAll`(FRawList())
    }

    @Test
    fun `test removeFirst`() {
        FListTestUtils.`test removeFirst`(FList())
        FListTestUtils.`test removeFirst`(FRawList())
    }

    @Test
    fun `test removeAll`() {
        FListTestUtils.`test removeAll`(FList())
        FListTestUtils.`test removeAll`(FRawList())
    }
}