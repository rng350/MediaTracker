package com.rng350.mediatracker.movies

data class MovieUserStatus(
    val isLiked: Boolean,
    val hasBeenWatched: Boolean,
    val isInWatchlist: Boolean
)
