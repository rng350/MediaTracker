package com.rng350.mediatracker.movies

import java.time.LocalDate

data class MovieDetails(
    val movieId: String,
    val movieTitle: String,
    val movieOverview: String,
    val movieReleaseDate: LocalDate? = null,
    val movieGenres: List<Genre>,
    val movieDirectors: List<MovieStaff>,
    val movieActors: List<MovieStaff>,
    val moviePosterUrl: String? = null
)
