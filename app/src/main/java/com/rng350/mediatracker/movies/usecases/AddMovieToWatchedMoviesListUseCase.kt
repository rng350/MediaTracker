package com.rng350.mediatracker.movies.usecases

import com.rng350.mediatracker.common.database.MovieDao
import com.rng350.mediatracker.movies.MovieDetails
import com.rng350.mediatracker.movies.MovieDetailsCache
import com.rng350.mediatracker.movies.WatchedMovie
import javax.inject.Inject

class AddMovieToWatchedMoviesListUseCase @Inject constructor(
    private val saveMovieDetailsToDatabseUseCase: SaveMovieDetailsToDatabseUseCase,
    private val movieDao: MovieDao,
    private val movieDetailsCache: MovieDetailsCache
) {
    suspend operator fun invoke(movieDetails: MovieDetails) {
        val movieId = movieDetails.movieId.toInt()
        if (movieDao.getMovie(movieId) == null) {
            saveMovieDetailsToDatabseUseCase(movieDetails)
        }
        movieDao.addMovieToWatchedList(WatchedMovie(movieId = movieDetails.movieId.toInt()))
        movieDetailsCache.updateCache(
            key = movieId.toString(),
            updatedValue = movieDetails.copy(hasBeenWatched = true)
        )
    }
}