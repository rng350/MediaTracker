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
import com.rng350.mediatracker.movies.usecases.AddMovieToWatchedMoviesListUseCase
import com.rng350.mediatracker.movies.usecases.RemoveMovieFromWatchedMoviesListUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit

@HiltWorker
class SetWatchedMovieWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val addMovieToWatchedMoviesListUseCase: AddMovieToWatchedMoviesListUseCase,
    private val removeMovieFromWatchedMoviesListUseCase: RemoveMovieFromWatchedMoviesListUseCase
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        val movieString = inputData.getString("movie") ?: return Result.failure()
        val actionString = inputData.getString("action") ?: return Result.failure()

        return try {
            val movie = Json.decodeFromString<MovieDetails>(movieString)
            val action = Json.decodeFromString<WatchMovieAction>(actionString)
            when(action) {
                WatchMovieAction.ADD -> addMovieToWatchedMoviesListUseCase(movie)
                WatchMovieAction.REMOVE -> removeMovieFromWatchedMoviesListUseCase(movie)
            }
            Result.success()
        } catch (e: Exception) {
            Log.e("SetWatchedMovieWorker", "SetWatchedMovieWorker failed: ${e.stackTraceToString()}")
            Result.retry()
        }
    }

    companion object {
        fun createWorkRequest(movieDetails: MovieDetails, action: WatchMovieAction): OneTimeWorkRequest {
            val data = workDataOf(
                "movie" to Json.encodeToString(MovieDetails.serializer(), movieDetails),
                "action" to Json.encodeToString(WatchMovieAction.serializer(), action)
            )
            return OneTimeWorkRequestBuilder<SetWatchedMovieWorker>()
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
    enum class WatchMovieAction {
        ADD,
        REMOVE
    }
}