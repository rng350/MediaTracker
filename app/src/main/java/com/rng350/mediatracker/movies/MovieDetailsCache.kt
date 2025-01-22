package com.rng350.mediatracker.movies

import com.rng350.mediatracker.common.Constants.MOVIE_DETAILS_CACHE_MAX_SIZE
import com.rng350.mediatracker.common.LRUCache
import javax.inject.Inject
import javax.inject.Singleton

// In-memory cache
@Singleton
class MovieDetailsCache @Inject constructor()
    : LRUCache<String, MovieDetails>(maxCacheSize = MOVIE_DETAILS_CACHE_MAX_SIZE)