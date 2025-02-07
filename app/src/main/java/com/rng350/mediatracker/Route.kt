package com.rng350.mediatracker

import com.rng350.mediatracker.common.encodeToBase64

sealed class Route(val routeName: String) {
    data class DiscoverMoviesScreen(
        val searchQuery: String = ""
    ): Route("discoverMoviesScreen/{searchQuery}") {
        override val navCommand: String
            get() = routeName.replace("{searchQuery}", searchQuery.encodeToBase64())
    }
    data class MovieDetailsScreen(
        val movieId: String = ""
    ): Route("movieDetails/{movieId}") {
        override val navCommand: String
            get() = routeName.replace("{movieId}", movieId)
    }
    data object MovieWatchlistScreen: Route("watchlistScreen")
    data object WatchedMoviesListScreen: Route("watchedMovies")
    data object FeaturedMoviesScreen: Route("featuredMovies")

    open val navCommand = routeName
}