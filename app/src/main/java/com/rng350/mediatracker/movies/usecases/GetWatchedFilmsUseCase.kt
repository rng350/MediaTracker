package com.rng350.mediatracker.movies.usecases

import com.rng350.mediatracker.common.database.MovieDao
import com.rng350.mediatracker.movies.MovieForDisplay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWatchedFilmsUseCase @Inject constructor(
    private val movieDao: MovieDao
) {
    operator fun invoke(): Flow<List<MovieForDisplay>> {
        return movieDao.getAllWatchedMovies()
    }
}