package com.rng350.mediatracker.movies.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.rng350.mediatracker.movies.MovieDetails
import com.rng350.mediatracker.movies.usecases.AddMovieToWatchlistUseCase
import com.rng350.mediatracker.movies.usecases.RemoveMovieFromWatchlistUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit

@HiltWorker
class MovieWatchlistWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val addMovieToWatchlistUseCase: AddMovieToWatchlistUseCase,
    private val removeMovieFromWatchlistUseCase: RemoveMovieFromWatchlistUseCase
): CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        val movieString = inputData.getString("movie") ?: return Result.failure()
        val actionString = inputData.getString("action") ?: return Result.failure()

        return try {
            val movieDetails = Json.decodeFromString<MovieDetails>(movieString)
            val action = Json.decodeFromString<WatchlistAction>(actionString)
            when (action) {
                WatchlistAction.ADD -> addMovieToWatchlistUseCase(movieDetails)
                WatchlistAction.REMOVE -> removeMovieFromWatchlistUseCase(movieDetails)
            }
            Result.success()
        } catch (e: Exception) {
            Log.e("MovieWatchlistWorker", "Watchlisting failed: ${e.stackTraceToString()}")
            Result.retry()
        }
    }

    companion object {
        fun createWorkRequest(movieDetails: MovieDetails, action: WatchlistAction): OneTimeWorkRequest {
            val data = workDataOf(
                "movie" to Json.encodeToString(MovieDetails.serializer(), movieDetails),
                "action" to Json.encodeToString(WatchlistAction.serializer(), action)
            )
            return OneTimeWorkRequestBuilder<MovieWatchlistWorker>()
                .setInputData(data)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.NOT_ROAMING)
                        .build()
                )
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    backoffDelay = 250,
                    timeUnit = TimeUnit.MILLISECONDS
                )
                .build()
        }
    }

    @Serializable
    enum class WatchlistAction {
        ADD,
        REMOVE
    }
}