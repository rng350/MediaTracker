package com.rng350.mediatracker.movies

import java.time.LocalDate

data class MovieForDisplay(
    val movieId: String,
    val movieTitle: String,
    val movieOriginalTitle: String?,
    val movieReleaseDate: LocalDate? = null,
    val moviePremise: String,
    val moviePosterUrl: String?
)
