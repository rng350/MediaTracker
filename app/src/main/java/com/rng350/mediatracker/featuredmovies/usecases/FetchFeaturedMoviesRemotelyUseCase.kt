package com.rng350.mediatracker.featuredmovies.usecases

import android.util.Log
import com.rng350.mediatracker.featuredmovies.FeaturedMovie
import com.rng350.mediatracker.networking.TMDBApi
import com.rng350.mediatracker.networking.TmdbApiTimer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException
import kotlin.coroutines.cancellation.CancellationException

abstract class FetchFeaturedMoviesRemotelyUseCase<T: FeaturedMovie>(
    protected open val tmdbApi: TMDBApi,
    protected open val tmdbApiTimer: TmdbApiTimer
) {
    // call the appropriate function from tmdbApi here
    abstract suspend fun fetchFeaturedMovies(): List<T>

    suspend operator fun invoke(): List<T> = withContext(Dispatchers.IO) {
        try {
            tmdbApiTimer.ensureTimeoutHasElapsed()
            return@withContext fetchFeaturedMovies()
        } catch (e: HttpException) {
            Log.e("API_ERROR", "HTTP error: ${e.code()}")
        } catch (e: IOException) {
            Log.e("API_ERROR", "Network error: ${e.localizedMessage}")
        } catch(e: CancellationException) {
            Log.e("API_ERROR", "CancellationException -- Parent coroutine was cancelled:. MESSAGE: ${e.message ?: "No message"}")
            throw e
        } catch (e: Exception) {
            Log.e("API_ERROR", "Unexpected error: ${e.localizedMessage}")
        }
        return@withContext emptyList()
    }
}