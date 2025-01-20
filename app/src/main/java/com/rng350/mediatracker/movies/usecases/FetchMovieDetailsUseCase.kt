package com.rng350.mediatracker.movies.usecases

import com.rng350.mediatracker.movies.MovieDetails
import com.rng350.mediatracker.networking.TMDBApi
import javax.inject.Inject

class FetchMovieDetailsUseCase @Inject constructor(
    private val tmdbApi: TMDBApi
) {
    sealed class MovieDetailsResult {
        data class Success(val movieDetails: MovieDetails): MovieDetailsResult()
        data object Error: MovieDetailsResult()
    }
}