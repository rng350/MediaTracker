package com.rng350.mediatracker.movies.usecases

import com.rng350.mediatracker.movies.MovieDetails
import com.rng350.mediatracker.movies.MovieDetailsCache
import com.rng350.mediatracker.networking.TMDBApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchMovieDetailsUseCase @Inject constructor(
    private val tmdbApi: TMDBApi,
    private val movieDetailsCache: MovieDetailsCache
) {
    sealed class MovieDetailsResult {
        data class Success(val movieDetails: MovieDetails): MovieDetailsResult()
        data object Error: MovieDetailsResult()
    }

    suspend operator fun invoke(movieId: String): MovieDetailsResult {
        return withContext(Dispatchers.IO) {
            val movieDetails = movieDetailsCache.get(movieId) ?: remotelyFetchMovieDetails(movieId)
            if (movieDetails != null) {
                movieDetailsCache.updateCache(key = movieDetails.movieId, updatedValue =  movieDetails)
                MovieDetailsResult.Success(movieDetails)
            }
            else MovieDetailsResult.Error
        }
    }

    private suspend fun remotelyFetchMovieDetails(movieId: String): MovieDetails? {
        return tmdbApi.getMovieDetails(movieId).body()?.toMovieDetailsForDisplay()
    }
}