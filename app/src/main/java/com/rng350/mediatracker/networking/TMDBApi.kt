package com.rng350.mediatracker.networking

import com.rng350.mediatracker.networking.movie.MoviesListSchema
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBApi {
    @GET("/search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("primary_release_year") releaseYear: String?,
        @Query("include_adult") includeAdultContent: Boolean = false,
        @Query("page") page: Int
    ): MoviesListSchema?
}