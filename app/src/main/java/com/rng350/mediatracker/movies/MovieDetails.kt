package com.rng350.mediatracker.movies

import android.net.Uri
import com.rng350.mediatracker.common.serializers.LocalDateSerializer
import com.rng350.mediatracker.common.serializers.UriSerializer
import java.time.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetails(
    val movieId: String,
    val movieTitle: String,
    val movieOriginalTitle: String?,
    val movieOverview: String,
    @Serializable(with = LocalDateSerializer::class)
    val movieReleaseDate: LocalDate? = null,
    val movieGenres: List<MovieGenre>,
    val movieDirectors: List<MovieStaff>,
    val movieActors: List<MovieActorAndRolesInFilm>,
    val moviePosterUrl: String? = null,
    @Serializable(with = UriSerializer::class)
    val moviePosterUri: Uri? = null,
    val isLiked: Boolean = false,
    val isOnWatchlist: Boolean = false,
    val hasBeenWatched: Boolean = false
)
