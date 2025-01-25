package com.rng350.mediatracker.movies

import java.time.LocalDate

data class MovieDetails(
    val movieId: String,
    val movieTitle: String,
    val movieOverview: String,
    val movieReleaseDate: LocalDate? = null,
    val movieMovieGenres: List<MovieGenre>,
    val movieDirectors: List<MovieStaff>,
    val movieActors: List<MovieActorAndRolesInFilm>,
    val moviePosterUrl: String? = null,
    val isLiked: Boolean = false,
    val isOnWatchlist: Boolean = false,
    val hasBeenWatched: Boolean = false
)
