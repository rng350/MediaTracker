package com.rng350.mediatracker.common

import okio.withLock
import java.util.concurrent.locks.ReentrantLock

abstract class LRUCache<K, V>(
    private val linkedHashMap: CustomLinkedHashMap<K, V>,
    private val maxCacheSize: Int? = null
) {
    private val lock = ReentrantLock()

    open fun updateCache(key: K, updatedValue: V) {
        lock.withLock {
            linkedHashMap.putAtFrontOfList(key, updatedValue)
            if (linkedHashMap.listSize > (maxCacheSize ?: Int.MAX_VALUE)) {
                linkedHashMap.removeLast()
            }
        }
    }

    open fun get(itemKey: K): V? {
        return lock.withLock {
            linkedHashMap.getElement(itemKey)
        }
    }
}