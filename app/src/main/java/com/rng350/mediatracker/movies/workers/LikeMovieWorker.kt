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
import com.rng350.mediatracker.movies.usecases.AddMovieToLikedListUseCase
import com.rng350.mediatracker.movies.usecases.RemoveMovieFromLikedListUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit

@HiltWorker
class LikeMovieWorker @AssistedInject constructor (
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val addMovieToLikedList: AddMovieToLikedListUseCase,
    private val removeMovieFromLikedList: RemoveMovieFromLikedListUseCase
): CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        val movieString = inputData.getString("movie") ?: return Result.failure()
        val actionString = inputData.getString("action") ?: return Result.failure()

        return try {
            val movie = Json.decodeFromString<MovieDetails>(movieString)
            val action = Json.decodeFromString<LikeMovieAction>(actionString)
            when (action) {
                LikeMovieAction.ADD -> addMovieToLikedList(movie)
                LikeMovieAction.REMOVE -> removeMovieFromLikedList(movie)
            }
            Result.success()
        }
        catch (e: Exception) {
            Log.e("LikeMovieWorker", "LikeMovieWorker failed: ${e.stackTraceToString()}")
            Result.retry()
        }
    }

    companion object {
        fun createWorkRequest(movieDetails: MovieDetails, action: LikeMovieAction): OneTimeWorkRequest {
            val data = workDataOf(
                "movie" to Json.encodeToString(MovieDetails.serializer(), movieDetails),
                "action" to Json.encodeToString(LikeMovieAction.serializer(), action)
            )
            return OneTimeWorkRequestBuilder<LikeMovieWorker>()
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
    enum class LikeMovieAction {
        ADD,
        REMOVE
    }
}