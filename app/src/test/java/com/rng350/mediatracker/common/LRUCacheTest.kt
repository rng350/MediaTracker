package com.rng350.mediatracker.common

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LRUCacheTest {
    @Test
    fun `Last item removed from hash when max amount exceeded`() {
        val linkedHashMap = CustomLinkedHashMap<Int, String>()
        val cache = object: LRUCache<Int, String>(maxCacheSize = 3, linkedHashMap = linkedHashMap) {}
        cache.updateCache(key=1, "One")
        cache.updateCache(key=2, "Two")
        cache.updateCache(key=3, "Three")
        cache.updateCache(key=4, "Four")
        cache.updateCache(key=5, "Five")
        cache.updateCache(key=6, "Six")
        assertNull(cache.get(1) ?: cache.get(2) ?: cache.get(3))
    }

    @Test
    fun `Last item in list updated when max amount exceeded`() {
        val linkedHashMap = CustomLinkedHashMap<Int, String>()
        val cache = object: LRUCache<Int, String>(maxCacheSize = 3, linkedHashMap = linkedHashMap) {}
        cache.updateCache(key=1, "One")
        cache.updateCache(key=2, "Two")
        cache.updateCache(key=3, "Three")
        cache.updateCache(key=4, "Four")
        cache.updateCache(key=5, "Five")
        cache.updateCache(key=6, "Six")
        assertEquals("Four", linkedHashMap.lastNode?.value)
    }

    @Test
    fun `Last item updated is at front of cache`() {
        val linkedHashMap = CustomLinkedHashMap<Int, String>()
        val cache = object: LRUCache<Int, String>(maxCacheSize = 3, linkedHashMap = linkedHashMap) {}
        cache.updateCache(key=1, "One")
        cache.updateCache(key=2, "Two")
        cache.updateCache(key=3, "Three")
        cache.updateCache(key=4, "Four")
        cache.updateCache(key=5, "Five")
        cache.updateCache(key=6, "Six")
        cache.updateCache(key=3, "Hi there")
        assertEquals("Hi there", linkedHashMap.firstNode?.value)
    }
}