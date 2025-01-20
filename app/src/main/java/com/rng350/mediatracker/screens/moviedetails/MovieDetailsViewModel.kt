package com.rng350.mediatracker.screens.moviedetails

import androidx.lifecycle.ViewModel
import com.rng350.mediatracker.movies.MovieDetails
import com.rng350.mediatracker.movies.usecases.FetchMovieDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val fetchMovieDetailsUseCase: FetchMovieDetailsUseCase
): ViewModel() {
    sealed class MovieDetailsResult {
        data class Success(val movieDetails: MovieDetails): MovieDetailsResult()
        data object None: MovieDetailsResult()
        data object Error: MovieDetailsResult()
    }

    private val _movieDetails = MutableStateFlow<MovieDetailsResult>(MovieDetailsResult.None)
    val movieDetails: StateFlow<MovieDetailsResult>
        get() = _movieDetails

    suspend fun fetchMovieDetails(movieId: String) {
        withContext(Dispatchers.Main.immediate) {
            fetchMovieDetailsUseCase
        }
    }
}