package com.rng350.mediatracker.common

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CustomLinkedHashMapTest {
    @Test
    fun `Get element has result when list contains something`() {
        val linkedHashMap = CustomLinkedHashMap<Int, String>()
        linkedHashMap.putAtFrontOfList(123, "Something")
        assertEquals("Something", linkedHashMap.getElement(123))
    }

    @Test
    fun `Get element has no result when list is empty`() {
        val linkedHashMap = CustomLinkedHashMap<Int, String>()
        assertNull(linkedHashMap.getElement(777))
    }

    @Test
    fun `First element is first in list`() {
        val linkedHashMap = CustomLinkedHashMap<Int, String>(
            listOf(
                CustomLinkedHashMap.KeyAndValue(key = 1, value = "One"),
                CustomLinkedHashMap.KeyAndValue(key = 2, value = "Two"),
                CustomLinkedHashMap.KeyAndValue(key = 3, value = "Three"),
                CustomLinkedHashMap.KeyAndValue(key = 4, value = "Four"),
                CustomLinkedHashMap.KeyAndValue(key = 5, value = "Five"),
                CustomLinkedHashMap.KeyAndValue(key = 6, value = "Six")
            )
        )
        assertEquals("One", linkedHashMap.firstNode?.value)
    }

    @Test
    fun `Last element is last in list`() {
        val linkedHashMap = CustomLinkedHashMap<Int, String>(
            listOf(
                CustomLinkedHashMap.KeyAndValue(key = 1, value = "One"),
                CustomLinkedHashMap.KeyAndValue(key = 2, value = "Two"),
                CustomLinkedHashMap.KeyAndValue(key = 3, value = "Three"),
                CustomLinkedHashMap.KeyAndValue(key = 4, value = "Four"),
                CustomLinkedHashMap.KeyAndValue(key = 5, value = "Five"),
                CustomLinkedHashMap.KeyAndValue(key = 6, value = "Six")
            )
        )
        assertEquals("Six", linkedHashMap.lastNode?.value)
    }

    @Test
    fun `First element is second in list after head removed twice`() {
        val linkedHashMap = CustomLinkedHashMap<Int, String>(
            listOf(
                CustomLinkedHashMap.KeyAndValue(key = 1, value = "One"),
                CustomLinkedHashMap.KeyAndValue(key = 2, value = "Two"),
                CustomLinkedHashMap.KeyAndValue(key = 3, value = "Three"),
                CustomLinkedHashMap.KeyAndValue(key = 4, value = "Four"),
                CustomLinkedHashMap.KeyAndValue(key = 5, value = "Five"),
                CustomLinkedHashMap.KeyAndValue(key = 6, value = "Six")
            )
        )
        linkedHashMap.removeFirst()
        linkedHashMap.removeFirst()
        assertEquals("Three", linkedHashMap.firstNode?.value)
    }

    @Test
    fun `Last element is fourth in list after head removed twice`() {
        val linkedHashMap = CustomLinkedHashMap<Int, String>(
            listOf(
                CustomLinkedHashMap.KeyAndValue(key = 1, value = "One"),
                CustomLinkedHashMap.KeyAndValue(key = 2, value = "Two"),
                CustomLinkedHashMap.KeyAndValue(key = 3, value = "Three"),
                CustomLinkedHashMap.KeyAndValue(key = 4, value = "Four"),
                CustomLinkedHashMap.KeyAndValue(key = 5, value = "Five"),
                CustomLinkedHashMap.KeyAndValue(key = 6, value = "Six")
            )
        )
        linkedHashMap.removeLast()
        linkedHashMap.removeLast()
        assertEquals("Four", linkedHashMap.lastNode?.value)
    }

    @Test
    fun `First element is latest put at head of list`() {
        val linkedHashMap = CustomLinkedHashMap<Int, String>(
            listOf(
                CustomLinkedHashMap.KeyAndValue(key = 1, value = "One"),
                CustomLinkedHashMap.KeyAndValue(key = 2, value = "Two"),
                CustomLinkedHashMap.KeyAndValue(key = 3, value = "Three"),
                CustomLinkedHashMap.KeyAndValue(key = 4, value = "Four"),
                CustomLinkedHashMap.KeyAndValue(key = 5, value = "Five"),
                CustomLinkedHashMap.KeyAndValue(key = 6, value = "Six")
            )
        )
        linkedHashMap.putAtFrontOfList(11, "Wrong")
        linkedHashMap.putAtFrontOfList(12, "Latest")
        assertEquals("Latest", linkedHashMap.firstNode?.value)
    }

    @Test
    fun `Last element is latest put at tail of list`() {
        val linkedHashMap = CustomLinkedHashMap<Int, String>(
            listOf(
                CustomLinkedHashMap.KeyAndValue(key = 1, value = "One"),
                CustomLinkedHashMap.KeyAndValue(key = 2, value = "Two"),
                CustomLinkedHashMap.KeyAndValue(key = 3, value = "Three"),
                CustomLinkedHashMap.KeyAndValue(key = 4, value = "Four"),
                CustomLinkedHashMap.KeyAndValue(key = 5, value = "Five"),
                CustomLinkedHashMap.KeyAndValue(key = 6, value = "Six")
            )
        )
        linkedHashMap.putAtEndOfList(11, "Wrong")
        linkedHashMap.putAtEndOfList(12, "Latest")
        assertEquals("Latest", linkedHashMap.lastNode?.value)
    }

    @Test
    fun `Item count is accurate`() {
        val linkedHashMap = CustomLinkedHashMap<Int, String>(
            listOf(
                CustomLinkedHashMap.KeyAndValue(key = 1, value = "One"),
                CustomLinkedHashMap.KeyAndValue(key = 2, value = "Two"),
                CustomLinkedHashMap.KeyAndValue(key = 3, value = "Three"),
                CustomLinkedHashMap.KeyAndValue(key = 4, value = "Four"),
                CustomLinkedHashMap.KeyAndValue(key = 5, value = "Five"),
                CustomLinkedHashMap.KeyAndValue(key = 6, value = "Six")
            )
        )
        linkedHashMap.putAtEndOfList(7, "Dummy")
        linkedHashMap.putAtEndOfList(8, "Dummy")
        linkedHashMap.putAtEndOfList(9, "Dummy")
        linkedHashMap.putAtEndOfList(10, "Dummy")
        linkedHashMap.putAtFrontOfList(11, "Dummy")
        linkedHashMap.putAtFrontOfList(12, "Dummy")
        linkedHashMap.removeElement(2)
        linkedHashMap.removeFirst()
        linkedHashMap.removeLast()
        linkedHashMap.putAtEndOfList(7, "Hello")
        assertEquals(9, linkedHashMap.listSize)
    }
}