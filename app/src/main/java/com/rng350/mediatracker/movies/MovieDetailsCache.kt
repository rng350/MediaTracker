package com.rng350.mediatracker.movies

import okio.withLock
import java.util.concurrent.locks.ReentrantLock
import javax.inject.Inject
import javax.inject.Singleton

// In-memory cache
@Singleton
class MovieDetailsCache @Inject constructor() {
    private val lock = ReentrantLock()
    private val movies = mutableMapOf<String, MovieDetails>()

    fun updateCache(updatedMovie: MovieDetails) {
        lock.withLock {
            movies[updatedMovie.movieId] = updatedMovie
        }
    }

    fun get(movieId: String): MovieDetails? {
        return lock.withLock {
            movies[movieId]
        }
    }
}