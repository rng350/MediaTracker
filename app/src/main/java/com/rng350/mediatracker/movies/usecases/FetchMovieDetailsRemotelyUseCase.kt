package com.rng350.mediatracker.movies.usecases

import android.util.Log
import com.rng350.mediatracker.common.database.MovieDao
import com.rng350.mediatracker.movies.MovieDetails
import com.rng350.mediatracker.networking.TMDBApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import okio.IOException
import retrofit2.HttpException
import kotlin.coroutines.cancellation.CancellationException

class FetchMovieDetailsRemotelyUseCase @Inject constructor(
    private val tmdbApi: TMDBApi,
    private val movieDao: MovieDao
) {
    suspend operator fun invoke(movieId: String): MovieDetails? = coroutineScope {
        return@coroutineScope try {
            val movieDetailsFetched = async {
                try {
                    tmdbApi.getMovieDetails(movieId).body()?.toMovieDetailsForDisplay()
                }
                catch (e: IOException) {
                    Log.e("FetchMovieDetailsRemotelyUseCase", "Network error while trying to remotely fetch movie details: ${e.localizedMessage}")
                    null
                }
            }
            val movieUserStatusFetched = async {
                try {
                    movieDao.getMovieUserStatus(movieId.toInt())
                }
                catch (e: Exception) {
                    Log.e("FetchMovieDetailsRemotelyUseCase", "Database error: ${e.localizedMessage}")
                    null
                }
            }
            val movieDetails = movieDetailsFetched.await()
            val movieUserStatus = movieUserStatusFetched.await()
            movieDetails?.copy(
                isLiked = movieUserStatus?.isLiked ?: false,
                isOnWatchlist = movieUserStatus?.isInWatchlist ?: false,
                hasBeenWatched = movieUserStatus?.hasBeenWatched ?: false
            )
        }
        catch (e: HttpException) {
            Log.e("FetchMovieDetailsRemotelyUseCase", "HTTP error: ${e.code()}")
            return@coroutineScope null
        } catch (e: IOException) {
            Log.e("FetchMovieDetailsRemotelyUseCase", "Network error :(((( : ${e.localizedMessage}")
            return@coroutineScope null
        } catch(e: CancellationException) {
            Log.e("FetchMovieDetailsRemotelyUseCase", "CancellationException -- Parent coroutine was cancelled:. MESSAGE: ${e.message ?: "No message"}")
            return@coroutineScope null
        } catch (e: Exception) {
            Log.e("FetchMovieDetailsRemotelyUseCase", "Unexpected error: ${e.localizedMessage}")
            return@coroutineScope null
        }
    }
}