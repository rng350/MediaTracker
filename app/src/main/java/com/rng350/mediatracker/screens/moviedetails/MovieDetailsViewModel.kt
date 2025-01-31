package com.rng350.mediatracker.screens.moviedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rng350.mediatracker.movies.MovieDetails
import com.rng350.mediatracker.movies.usecases.AddMovieToWatchedMoviesListUseCase
import com.rng350.mediatracker.movies.usecases.AddMovieToWatchlistUseCase
import com.rng350.mediatracker.movies.usecases.FetchMovieDetailsUseCase
import com.rng350.mediatracker.movies.usecases.RemoveMovieFromWatchedMoviesListUseCase
import com.rng350.mediatracker.movies.usecases.RemoveMovieFromWatchlistUseCase
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
    private val addMovieToWatchlistUseCase: AddMovieToWatchlistUseCase,
    private val removeMovieFromWatchlistUseCase: RemoveMovieFromWatchlistUseCase,
    private val addMovieToWatchedMoviesListUseCase: AddMovieToWatchedMoviesListUseCase,
    private val removeMovieFromWatchedMoviesListUseCase: RemoveMovieFromWatchedMoviesListUseCase
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

    fun toggleToWatchList(movieId: String) {
        (movieDetails.value as? MovieDetailsResult.Success)
            ?.movieDetails
            ?.let { movie ->
                viewModelScope.launch {
                    when (movieIsOnWatchlist.value) {
                        true -> removeMovieFromWatchlistUseCase(movie)
                        false -> addMovieToWatchlistUseCase(movie)
                    }
                }
                _movieIsOnWatchlist.update { !_movieIsOnWatchlist.value }
            }
    }

    fun toggleLikeMovie(movieId: String) {
        _movieIsLiked.update { !_movieIsLiked.value }
    }

    fun toggleWatchedMovie(movieId: String) {
        (movieDetails.value as? MovieDetailsResult.Success)
            ?.movieDetails
            ?.let { movie ->
                viewModelScope.launch {
                    when (movieHasBeenWatched.value) {
                        true -> removeMovieFromWatchedMoviesListUseCase(movie)
                        false -> addMovieToWatchedMoviesListUseCase(movie)
                    }
                }
                _movieHasBeenWatched.update { !_movieHasBeenWatched.value }
            }
    }
}