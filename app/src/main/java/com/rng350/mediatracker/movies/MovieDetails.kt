package com.rng350.mediatracker.movies

import android.net.Uri
import java.time.LocalDate

data class MovieDetails(
    val movieId: String,
    val movieTitle: String,
    val movieOverview: String,
    val movieReleaseDate: LocalDate? = null,
    val movieDirectors: List<String>,
    val movieWriters: List<String>,
    val movieActors: List<String>,
    val moviePosterUri: Uri? = null
)
