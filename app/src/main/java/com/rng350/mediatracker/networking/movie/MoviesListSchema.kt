package com.rng350.mediatracker.networking.movie

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MoviesListSchema(
    @Json(name="results") val movies: List<MovieSchema>
)
