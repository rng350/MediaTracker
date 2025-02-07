package com.rng350.mediatracker.screens.featuredmovies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rng350.mediatracker.featuredmovies.usecases.FetchFeaturedMoviesUseCase
import com.rng350.mediatracker.featuredmovies.usecases.FetchNowPlayingMoviesUseCase
import com.rng350.mediatracker.featuredmovies.usecases.FetchThisWeeksTrendingMoviesUseCase
import com.rng350.mediatracker.featuredmovies.usecases.FetchTodaysTrendingMoviesUseCase
import com.rng350.mediatracker.featuredmovies.usecases.FetchUpcomingMoviesUseCase
import com.rng350.mediatracker.movies.MovieForDisplay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeaturedMoviesViewModel @Inject constructor(
    private val fetchNowPlayingMoviesUseCase: FetchNowPlayingMoviesUseCase,
    private val fetchUpcomingMoviesUseCase: FetchUpcomingMoviesUseCase,
    private val fetchTodaysTrendingMoviesUseCase: FetchTodaysTrendingMoviesUseCase,
    private val fetchThisWeeksTrendingMoviesUseCase: FetchThisWeeksTrendingMoviesUseCase
): ViewModel() {
    private val _moviesNowPlaying: MutableStateFlow<MoviesNowPlayingResult> = MutableStateFlow(MoviesNowPlayingResult.None)
    private val _moviesTrendingToday: MutableStateFlow<MoviesTrendingTodayResult> = MutableStateFlow(MoviesTrendingTodayResult.None)
    private val _moviesTrendingThisWeek: MutableStateFlow<MoviesTrendingThisWeekResult> = MutableStateFlow(MoviesTrendingThisWeekResult.None)
    private val _moviesUpcoming: MutableStateFlow<MoviesUpcomingResult> = MutableStateFlow(MoviesUpcomingResult.None)

    val moviesNowPlaying: StateFlow<MoviesNowPlayingResult> get() = _moviesNowPlaying
    val moviesTrendingToday: StateFlow<MoviesTrendingTodayResult> get() = _moviesTrendingToday
    val moviesTrendingThisWeek: StateFlow<MoviesTrendingThisWeekResult> get() = _moviesTrendingThisWeek
    val moviesUpcoming: StateFlow<MoviesUpcomingResult> get() = _moviesUpcoming

    init {
        viewModelScope.launch {
            fetchNowPlayingMoviesUseCase()
                .collect { result ->
                    when (result) {
                        is FetchFeaturedMoviesUseCase.FeaturedMoviesResult.Success -> {
                            _moviesNowPlaying.update { MoviesNowPlayingResult.Success(result.movies.map { it.toMovieForDisplay() }) }
                        }
                        is FetchFeaturedMoviesUseCase.FeaturedMoviesResult.Error -> {
                            _moviesNowPlaying.update { MoviesNowPlayingResult.Error }
                        }
                    }
                }
        }
        viewModelScope.launch {
            fetchUpcomingMoviesUseCase()
                .collect { result ->
                    when (result) {
                        is FetchFeaturedMoviesUseCase.FeaturedMoviesResult.Success -> {
                            _moviesUpcoming.update { MoviesUpcomingResult.Success(result.movies.map { it.toMovieForDisplay() }) }
                        }
                        is FetchFeaturedMoviesUseCase.FeaturedMoviesResult.Error -> {
                            _moviesUpcoming.update { MoviesUpcomingResult.Error }
                        }
                    }
                }
        }
        viewModelScope.launch {
            fetchTodaysTrendingMoviesUseCase()
                .collect { result ->
                    when (result) {
                        is FetchFeaturedMoviesUseCase.FeaturedMoviesResult.Success -> {
                            _moviesTrendingToday.update { MoviesTrendingTodayResult.Success(result.movies.map { it.toMovieForDisplay() }) }
                        }
                        is FetchFeaturedMoviesUseCase.FeaturedMoviesResult.Error -> {
                            _moviesTrendingToday.update { MoviesTrendingTodayResult.Error }
                        }
                    }
                }
        }
        viewModelScope.launch {
            fetchThisWeeksTrendingMoviesUseCase()
                .collect { result ->
                    when (result) {
                        is FetchFeaturedMoviesUseCase.FeaturedMoviesResult.Success -> {
                            _moviesTrendingThisWeek.update { MoviesTrendingThisWeekResult.Success(result.movies.map { it.toMovieForDisplay() }) }
                        }
                        is FetchFeaturedMoviesUseCase.FeaturedMoviesResult.Error -> {
                            _moviesTrendingThisWeek.update { MoviesTrendingThisWeekResult.Error }
                        }
                    }
                }
        }
    }

    sealed class MoviesNowPlayingResult {
        data class Success(val movies: List<MovieForDisplay>): MoviesNowPlayingResult()
        data object None: MoviesNowPlayingResult()
        data object Error: MoviesNowPlayingResult()
    }
    sealed class MoviesTrendingTodayResult {
        data class Success(val movies: List<MovieForDisplay>): MoviesTrendingTodayResult()
        data object None: MoviesTrendingTodayResult()
        data object Error: MoviesTrendingTodayResult()
    }
    sealed class MoviesTrendingThisWeekResult {
        data class Success(val movies: List<MovieForDisplay>): MoviesTrendingThisWeekResult()
        data object None: MoviesTrendingThisWeekResult()
        data object Error: MoviesTrendingThisWeekResult()
    }
    sealed class MoviesUpcomingResult {
        data class Success(val movies: List<MovieForDisplay>): MoviesUpcomingResult()
        data object None: MoviesUpcomingResult()
        data object Error: MoviesUpcomingResult()
    }
}