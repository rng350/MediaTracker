package com.rng350.mediatracker.movies.usecases

import com.rng350.mediatracker.common.database.MovieDao
import com.rng350.mediatracker.movies.MovieDetails
import com.rng350.mediatracker.movies.WatchedMovie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoveMovieFromWatchedMoviesListUseCase @Inject constructor(
    private val movieDao: MovieDao
) {
    suspend operator fun invoke(movieDetails: MovieDetails) = withContext(Dispatchers.IO) {
        movieDao.removeMovieFromWatchedlist(WatchedMovie(movieDetails.movieId.toInt()))
    }
}