package com.rng350.mediatracker.movies.usecases

import com.rng350.mediatracker.common.database.MovieDao
import com.rng350.mediatracker.movies.MovieDetails
import com.rng350.mediatracker.networking.TMDBApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import okio.IOException

class FetchMovieDetailsRemotelyUseCase @Inject constructor(
    private val tmdbApi: TMDBApi,
    private val movieDao: MovieDao
) {
    suspend operator fun invoke(movieId: String): MovieDetails? = coroutineScope {
        return@coroutineScope try {
            val movieDetailsFetched = async {
                tmdbApi.getMovieDetails(movieId).body()?.toMovieDetailsForDisplay()
            }
            val movieUserStatusFetched = async {
                movieDao.getMovieUserStatus(movieId.toInt())
            }
            val movieDetails = movieDetailsFetched.await()
            val movieUserStatus = movieUserStatusFetched.await()
            movieDetails?.copy(
                isLiked = movieUserStatus.isLiked,
                isOnWatchlist = movieUserStatus.isInWatchlist,
                hasBeenWatched = movieUserStatus.hasBeenWatched
            )
        }
        // Network connectivity issue
        catch(e: IOException) {
            null
        }
    }
}