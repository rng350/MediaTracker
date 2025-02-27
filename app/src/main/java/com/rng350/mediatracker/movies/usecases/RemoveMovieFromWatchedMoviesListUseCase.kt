package com.rng350.mediatracker.movies.usecases

import com.rng350.mediatracker.common.database.MovieDao
import com.rng350.mediatracker.movies.MovieDetails
import com.rng350.mediatracker.movies.MovieDetailsCache
import com.rng350.mediatracker.movies.WatchedMovie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoveMovieFromWatchedMoviesListUseCase @Inject constructor(
    private val movieDao: MovieDao,
    private val movieDetailsCache: MovieDetailsCache
) {
    suspend operator fun invoke(movieDetails: MovieDetails) = withContext(Dispatchers.IO) {
        movieDao.removeMovieFromWatchedlist(WatchedMovie(movieDetails.movieId.toInt()))
        movieDetailsCache.updateCache(
            key = movieDetails.movieId,
            updatedValue = movieDetails.copy(hasBeenWatched = false)
        )
    }
}