package com.rng350.mediatracker.movies.usecases

import com.rng350.mediatracker.common.database.MovieDao
import com.rng350.mediatracker.movies.LikedMovie
import com.rng350.mediatracker.movies.MovieDetails
import com.rng350.mediatracker.movies.MovieDetailsCache
import javax.inject.Inject

class RemoveMovieFromLikedListUseCase @Inject constructor(
    private val movieDao: MovieDao,
    private val movieDetailsCache: MovieDetailsCache
) {
    suspend operator fun invoke(movieDetails: MovieDetails) {
        movieDao.removeMovieFromLikedList(LikedMovie(movieDetails.movieId.toInt()))
        movieDetailsCache.updateCache(
            key = movieDetails.movieId,
            updatedValue = movieDetails.copy(isLiked = false)
        )
    }
}