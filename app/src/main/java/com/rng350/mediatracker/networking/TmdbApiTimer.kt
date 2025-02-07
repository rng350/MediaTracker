package com.rng350.mediatracker.networking

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TmdbApiTimer @Inject constructor() {
    private val mutex = Mutex()
    private var lastApiRequestNano = 0L

    suspend fun ensureTimeoutHasElapsed() {
        mutex.withLock {
            if (!hasEnoughTimePassed()) {
                delay(THROTTLE_TIMEOUT_MS - ((System.nanoTime() - lastApiRequestNano) * 1_000_000))
            }
            lastApiRequestNano = System.nanoTime()
        }
    }

    private fun hasEnoughTimePassed(): Boolean {
        return System.nanoTime() - lastApiRequestNano > THROTTLE_TIMEOUT_MS * 1_000_000
    }

    companion object {
        private const val THROTTLE_TIMEOUT_MS = 100L
    }
}