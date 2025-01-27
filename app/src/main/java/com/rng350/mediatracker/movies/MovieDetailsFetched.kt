package com.rng350.mediatracker.movies

import android.net.Uri
import java.time.LocalDate

// flat list, still gotta filter this after fetching
data class MovieDetailsFetched(
    val movieId: Int,
    val movieTitle: String,
    val movieOriginalTitle: String?,
    val movieReleaseDate: LocalDate?,
    val moviePremise: String,
    val moviePosterUri: Uri?,
    val moviePosterUrl: String?,
    val isLiked: Boolean,
    val isWatchlisted: Boolean,
    val hasBeenWatched: Boolean,
    val genreId: Int?,
    val genreName: String?,
    val actingRoleCastId: Int?,
    val actingRoleCharacterName: String?,
    val actingRoleOrderOfImportance: Int?,
    val actorPersonId: Int?,
    val actorPersonName: String?,
    val actorPersonProfilePicUrl: String?,
    val actorPersonProfilePicUri: Uri?,
    val directorPersonId: Int?,
    val directorPersonName: String?,
    val directorPersonProfilePicUrl: String?,
    val directorPersonProfilePicUri: Uri?
)