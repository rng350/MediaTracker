package com.rng350.mediatracker.movies.usecases

import com.rng350.mediatracker.common.database.MovieDao
import com.rng350.mediatracker.movies.MovieDetails
import com.rng350.mediatracker.movies.MovieDetailsCache
import com.rng350.mediatracker.movies.WatchlistedMovie
import javax.inject.Inject

class AddMovieToWatchlistUseCase @Inject constructor(
    private val saveMovieDetailsToDatabseUseCase: SaveMovieDetailsToDatabseUseCase,
    private val movieDao: MovieDao,
    private val movieDetailsCache: MovieDetailsCache
) {
    suspend operator fun invoke(movieDetails: MovieDetails) {
        val movieId = movieDetails.movieId.toInt()
        if (movieDao.getMovie(movieId) == null) {
            saveMovieDetailsToDatabseUseCase(movieDetails)
        }
        movieDao.addMovieToWatchlist(WatchlistedMovie(movieId))
        movieDetailsCache.updateCache(
            key = movieId.toString(),
            updatedValue = movieDetails.copy(isOnWatchlist = true)
        )
    }
}