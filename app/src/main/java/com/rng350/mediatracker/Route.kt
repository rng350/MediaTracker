package com.rng350.mediatracker

sealed class Route(val routeName: String) {
    data object DiscoverMoviesScreen: Route("discoverMoviesScreen")
    data class MovieDetailsScreen(
        val movieId: String = ""
    ): Route("movieDetails/{movieId}") {
        override val navCommand: String
            get() = routeName.replace("{movieId}", movieId)
    }
    data object MovieWatchlistScreen: Route("watchlistScreen")
    data object WatchedMoviesListScreen: Route("watchedMovies")

    open val navCommand = routeName
}