package com.rng350.mediatracker.networking

import com.rng350.mediatracker.networking.movie.MovieDetailsSchema
import com.rng350.mediatracker.networking.movie.MoviesListSchema
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApi {
/*    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String?,
        //@Query("primary_release_year") releaseYear: String?,
        @Query("include_adult") includeAdultContent: Boolean?,
        @Query("page") page: Int?
    ): MoviesListSchema?*/

    /*@GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String?,
        //@Query("primary_release_year") releaseYear: String?,
        @Query("include_adult") includeAdultContent: Boolean?,
        @Query("page") page: Int?
    ): ResponseBody*/
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String?,
        //@Query("primary_release_year") releaseYear: String?,
        @Query("include_adult") includeAdultContent: Boolean?,
        @Query("page") page: Int?
    ): Response<MoviesListSchema>

    @GET("movie/{movie_id}?append_to_response=credits")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: String?
    ): Response<MovieDetailsSchema>

    @GET("movie/now_playing")
    suspend fun getMoviesPlayingNow(
        @Query("page") page: Int?
    ): Response<MoviesListSchema>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("page") page: Int?
    ): Response<MoviesListSchema>

    @GET("trending/movie/day")
    suspend fun getTrendingMoviesOfDay(
        @Query("page") page: Int?
    ): Response<MoviesListSchema>

    @GET("trending/movie/week")
    suspend fun getTrendingMoviesOfWeek(
        @Query("page") page: Int?
    ): Response<MoviesListSchema>
}