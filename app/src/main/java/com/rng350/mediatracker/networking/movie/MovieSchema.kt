package com.rng350.mediatracker.networking.movie

import com.rng350.mediatracker.common.Constants.TMDB_IMAGE_BASE_URL
import com.rng350.mediatracker.movies.MovieForDisplay
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@JsonClass(generateAdapter = true)
data class MovieSchema(
    @Json(name="id") val id: String,
    @Json(name="title") val title: String,
    @Json(name="original_title") val originalTitle: String? = null,
    @Json(name="release_date") val releaseDate: String? = null,
    @Json(name="overview") val overview: String? = null,
    @Json(name="poster_path") val posterPath: String? = null
) {
    fun toMovieForDisplay(): MovieForDisplay {
        return MovieForDisplay(
            movieId = this.id,
            movieTitle = this.title,
            moviePremise = this.overview ?: "",
            movieReleaseDate = if (!this.releaseDate.isNullOrEmpty()) LocalDate.parse(this.releaseDate, DateTimeFormatter.ISO_LOCAL_DATE) else null,
            moviePosterUrl = if (!this.posterPath.isNullOrEmpty()) TMDB_IMAGE_BASE_URL + this.posterPath else null
        )
    }
}