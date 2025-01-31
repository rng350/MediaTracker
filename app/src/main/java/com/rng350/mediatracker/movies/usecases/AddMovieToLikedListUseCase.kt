package com.rng350.mediatracker.movies.usecases

import com.rng350.mediatracker.common.database.MovieDao
import com.rng350.mediatracker.movies.LikedMovie
import com.rng350.mediatracker.movies.MovieDetails
import com.rng350.mediatracker.movies.MovieDetailsCache
import javax.inject.Inject

class AddMovieToLikedListUseCase @Inject constructor(
    private val saveMovieDetailsToDatabseUseCase: SaveMovieDetailsToDatabseUseCase,
    private val movieDao: MovieDao,
    private val movieDetailsCache: MovieDetailsCache
) {
    suspend operator fun invoke(movieDetails: MovieDetails) {
        val movieId = movieDetails.movieId.toInt()
        if (movieDao.getMovie(movieId) == null) {
            saveMovieDetailsToDatabseUseCase(movieDetails)
        }
        movieDao.addMovieToLikedList(LikedMovie(movieId))
        movieDetailsCache.updateCache(
            key = movieDetails.movieId,
            updatedValue = movieDetails.copy(hasBeenWatched = false)
        )
    }
}