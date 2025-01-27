package com.rng350.mediatracker.movies

import android.net.Uri
import java.time.LocalDate

data class MovieForDisplay(
    val movieId: Int,
    val movieTitle: String,
    val movieOriginalTitle: String?,
    val movieReleaseDate: LocalDate? = null,
    val moviePremise: String,
    val moviePosterUrl: String?,
    val moviePosterUri: Uri? = null
)
