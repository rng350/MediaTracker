package com.rng350.mediatracker

sealed class Route(val routeName: String) {
    data object DiscoverMoviesScreen: Route("discoverMoviesScreen")
    data class MovieDetailsScreen(
        val movieId: String = ""
    ): Route("movieDetails/{movieId}") {
        override val navCommand: String
            get() = routeName.replace("{movieId}", movieId)
    }

    open val navCommand = routeName
}