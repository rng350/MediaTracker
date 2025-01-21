package com.rng350.mediatracker.networking.movie

import com.rng350.mediatracker.common.Constants.NUMBER_OF_ACTORS_IN_MOVIE_DETAILS
import com.rng350.mediatracker.common.Constants.TMDB_IMAGE_BASE_URL
import com.rng350.mediatracker.common.toLocalDate
import com.rng350.mediatracker.movies.Genre
import com.rng350.mediatracker.movies.MovieDetails
import com.rng350.mediatracker.movies.MovieStaff
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


/*
*id: string
title: string
original_title: string
tagline: string
overview: string
genres
	id: string
	name: string
original_language: string
poster_path: string
release_date: string
runtime: string
cast
	id: string
	cast_id: string <- id of the credit itself, as there can be more than one cast credit per person
	name: string
	profile_path: string
	character: string <- character name
crew
	id: string
	name: string
	job: string <- look for "Director" or "Writer"

* */
@JsonClass(generateAdapter = true)
data class MovieDetailsSchema(
    @Json(name="id") val movieId: String?,
    @Json(name="title") val movieTitle: String?,
    @Json(name="original_title") val originalMovieTitle: String?,
    @Json(name="release_date") val movieReleaseDate: String?,
    @Json(name="tagline") val movieTagline: String?,
    @Json(name="overview") val movieOverview: String?,
    @Json(name="genres") val movieGenres: List<MovieGenreSchema>?,
    @Json(name="credits") val movieCredits: MovieCredits?,
    @Json(name="poster_path") val posterPath: String?
) {
    fun toMovieDetailsForDisplay(): MovieDetails {
        return MovieDetails(
            movieId = movieId ?: "",
            movieTitle = movieTitle ?: "Untitled Movie",
            movieOverview = movieOverview ?: "No",
            movieReleaseDate = movieReleaseDate?.toLocalDate(),
            movieGenres = movieGenres?.map { Genre(genreId = it.genreId, genreName = it.genreName) } ?: emptyList(),
            movieDirectors = movieCredits
                ?.movieCrew
                ?.filter { it.job == "Director" }
                ?.map { MovieStaff(it.personId.toInt(), it.personName) }
                    ?: emptyList(),
            movieActors = movieCredits
                ?.movieCast
                ?.take(NUMBER_OF_ACTORS_IN_MOVIE_DETAILS)
                ?.map { MovieStaff(it.actorId.toInt(), it.actorName) }
                    ?: emptyList(),
            moviePosterUrl = if (!posterPath.isNullOrEmpty()) "${TMDB_IMAGE_BASE_URL}$posterPath" else null
        )
    }
}

@JsonClass(generateAdapter = true)
data class MovieGenreSchema(
    @Json(name="id") val genreId: Int,
    @Json(name="name") val genreName: String
)

@JsonClass(generateAdapter = true)
data class MovieCredits(
    @Json(name="cast") val movieCast: List<MovieCastSchema>?,
    @Json(name="crew") val movieCrew: List<MovieCrewSchema>?
)

@JsonClass(generateAdapter = true)
data class MovieCastSchema(
    @Json(name="id") val actorId: String,
    @Json(name="cast_id") val castId: String,
    @Json(name="name") val actorName: String,
    @Json(name="profile_path") val actorProfilePic: String?,
    @Json(name="character") val characterName: String?
)

@JsonClass(generateAdapter = true)
data class MovieCrewSchema(
    @Json(name="id") val personId: String,
    @Json(name="name") val personName: String,
    @Json(name="job") val job: String?
)