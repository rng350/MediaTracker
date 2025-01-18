package com.rng350.mediatracker.networking.movie

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieSchema(
    @Json(name="id") val id: Int,
    @Json(name="title") val title: String,
    @Json(name="original_title") val originalTitle: String,
    @Json(name="release_date") val releaseDate: String,
    @Json(name="overview") val overview: String,
    @Json(name="poster_path") val posterPath: String
)