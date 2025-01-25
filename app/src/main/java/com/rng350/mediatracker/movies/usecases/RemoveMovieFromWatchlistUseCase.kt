package com.rng350.mediatracker.movies.usecases

import com.rng350.mediatracker.common.database.MovieDao
import com.rng350.mediatracker.movies.MovieDetails
import com.rng350.mediatracker.movies.WatchlistedMovie
import javax.inject.Inject

class RemoveMovieFromWatchlistUseCase @Inject constructor(
    private val movieDao: MovieDao
) {
    suspend operator fun invoke(movieDetails: MovieDetails) {
        movieDao.removeMovieFromWatchlist(WatchlistedMovie(movieDetails.movieId.toInt()))
    }
}