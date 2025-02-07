package com.rng350.mediatracker.featuredmovies

import okio.withLock
import java.util.concurrent.locks.ReentrantLock

abstract class FeaturedMoviesCache<T: FeaturedMovie> {
    val lock: ReentrantLock = ReentrantLock()
    var featuredMoviesList: List<T>? = null

    fun updateCache(newFeaturedMovies: List<T>) {
        lock.withLock {
            featuredMoviesList = newFeaturedMovies
        }
    }

    fun getFeaturedMovies() = featuredMoviesList?.toList()
}