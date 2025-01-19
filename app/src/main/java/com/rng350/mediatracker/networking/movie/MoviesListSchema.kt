package com.rng350.mediatracker.networking.movie

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MoviesListSchema(
    @Json(name="page") val page: Int? = null,
    @Json(name="results") val movies: List<MovieSchema>? = null,
    @Json(name="total_pages") val totalPages: Int? = null,
    @Json(name="total_results") val totalResults: Int? = null
)
