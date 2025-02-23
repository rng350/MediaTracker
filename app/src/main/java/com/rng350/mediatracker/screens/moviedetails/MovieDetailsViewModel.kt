package com.rng350.mediatracker.screens.moviedetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.rng350.mediatracker.movies.MovieDetails
import com.rng350.mediatracker.movies.usecases.AddMovieToLikedListUseCase
import com.rng350.mediatracker.movies.usecases.AddMovieToWatchedMoviesListUseCase
import com.rng350.mediatracker.movies.usecases.AddMovieToWatchlistUseCase
import com.rng350.mediatracker.movies.usecases.FetchMovieDetailsUseCase
import com.rng350.mediatracker.movies.usecases.RemoveMovieFromLikedListUseCase
import com.rng350.mediatracker.movies.usecases.RemoveMovieFromWatchedMoviesListUseCase
import com.rng350.mediatracker.movies.usecases.RemoveMovieFromWatchlistUseCase
import com.rng350.mediatracker.movies.workers.LikeMovieWorker
import com.rng350.mediatracker.movies.workers.MovieWatchlistWorker
import com.rng350.mediatracker.movies.workers.SetWatchedMovieWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val fetchMovieDetailsUseCase: FetchMovieDetailsUseCase,
    private val workManager: WorkManager,
): ViewModel() {
    sealed class MovieDetailsResult {
        data class Success(val movieDetails: MovieDetails): MovieDetailsResult()
        data object None: MovieDetailsResult()
        data object Error: MovieDetailsResult()
    }

    private val _movieDetails = MutableStateFlow<MovieDetailsResult>(MovieDetailsResult.None)
    val movieDetails: StateFlow<MovieDetailsResult>
        get() = _movieDetails

    private val _movieIsLiked = MutableStateFlow<Boolean>(false)
    val movieIsLiked: StateFlow<Boolean> get() = _movieIsLiked
    private val _movieIsOnWatchlist = MutableStateFlow<Boolean>(false)
    val movieIsOnWatchlist: StateFlow<Boolean> get() = _movieIsOnWatchlist
    private val _movieHasBeenWatched = MutableStateFlow<Boolean>(false)
    val movieHasBeenWatched: StateFlow<Boolean> get() = _movieHasBeenWatched

    private val movieDetailsWorkTag = "movieDetails"

    private fun getCurrentMovieDetails(movie: MovieDetails): MovieDetails =
        movie.copy(
            isLiked = movieIsLiked.value,
            isOnWatchlist = movieIsOnWatchlist.value,
            hasBeenWatched = movieHasBeenWatched.value
        )

    suspend fun fetchMovieDetails(movieId: String) {
        withContext(Dispatchers.Main.immediate) {
            val fetchResult = fetchMovieDetailsUseCase(movieId)
            val movieDetailsDisplayResult = when (fetchResult) {
                is FetchMovieDetailsUseCase.MovieDetailsResult.Error -> {
                    MovieDetailsResult.Error
                }
                is FetchMovieDetailsUseCase.MovieDetailsResult.Success -> {
                    MovieDetailsResult.Success(fetchResult.movieDetails)
                }
            }
            when (movieDetailsDisplayResult) {
                is MovieDetailsResult.Success -> {
                    _movieIsLiked.update { movieDetailsDisplayResult.movieDetails.isLiked }
                    _movieIsOnWatchlist.update { movieDetailsDisplayResult.movieDetails.isOnWatchlist }
                    _movieHasBeenWatched.update { movieDetailsDisplayResult.movieDetails.hasBeenWatched }
                }
                else -> {}
            }
            _movieDetails.update { movieDetailsDisplayResult }
        }
    }

    fun toggleToWatchList() {
        (movieDetails.value as? MovieDetailsResult.Success)
            ?.movieDetails
            ?.let { movie ->
                _movieIsOnWatchlist.update { !_movieIsOnWatchlist.value }
                val workRequest = MovieWatchlistWorker.createWorkRequest(
                    getCurrentMovieDetails(movie),
                    if (movieIsOnWatchlist.value) MovieWatchlistWorker.WatchlistAction.ADD
                    else MovieWatchlistWorker.WatchlistAction.REMOVE
                )
                workManager.enqueueUniqueWork(movieDetailsWorkTag, ExistingWorkPolicy.APPEND, workRequest)
            }
    }

    fun toggleLikeMovie() {
        (movieDetails.value as? MovieDetailsResult.Success)
            ?.movieDetails
            ?.let { movie ->
                _movieIsLiked.update { !_movieIsLiked.value }
                val workRequest = LikeMovieWorker.createWorkRequest(
                    getCurrentMovieDetails(movie),
                    if (movieIsLiked.value) LikeMovieWorker.LikeMovieAction.ADD
                    else LikeMovieWorker.LikeMovieAction.REMOVE
                )
                workManager.enqueueUniqueWork(movieDetailsWorkTag, ExistingWorkPolicy.APPEND, workRequest)
            }
    }

    fun toggleWatchedMovie() {
        (movieDetails.value as? MovieDetailsResult.Success)
            ?.movieDetails
            ?.let { movie ->
                _movieHasBeenWatched.update { !_movieHasBeenWatched.value }
                val workRequest = SetWatchedMovieWorker.createWorkRequest(
                    getCurrentMovieDetails(movie),
                    if (movieHasBeenWatched.value) SetWatchedMovieWorker.WatchMovieAction.ADD
                    else SetWatchedMovieWorker.WatchMovieAction.REMOVE
                )
                workManager.enqueueUniqueWork(movieDetailsWorkTag, ExistingWorkPolicy.APPEND, workRequest)
            }
    }
}