package com.rng350.mediatracker.movies

import android.net.Uri
import java.time.LocalDate

data class MovieDetails(
    val movieId: String,
    val movieTitle: String,
    val movieOriginalTitle: String?,
    val movieOverview: String,
    val movieReleaseDate: LocalDate? = null,
    val movieGenres: List<MovieGenre>,
    val movieDirectors: List<MovieStaff>,
    val movieActors: List<MovieActorAndRolesInFilm>,
    val moviePosterUrl: String? = null,
    val moviePosterUri: Uri? = null,
    val isLiked: Boolean = false,
    val isOnWatchlist: Boolean = false,
    val hasBeenWatched: Boolean = false
)
