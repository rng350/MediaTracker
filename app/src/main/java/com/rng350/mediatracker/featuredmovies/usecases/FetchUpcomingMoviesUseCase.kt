package com.rng350.mediatracker.featuredmovies.usecases

import com.rng350.mediatracker.common.database.MovieDao
import com.rng350.mediatracker.featuredmovies.FeaturedMovieUpcoming
import com.rng350.mediatracker.featuredmovies.FeaturedMoviesUpcomingCache
import javax.inject.Inject

class FetchUpcomingMoviesUseCase @Inject constructor(
    fetchUpcomingMoviesRemotelyUseCase: FetchUpcomingMoviesRemotelyUseCase,
    featuredMoviesUpcomingCache: FeaturedMoviesUpcomingCache,
    override val movieDao: MovieDao
): FetchFeaturedMoviesUseCase<FeaturedMovieUpcoming>(
    fetchUpcomingMoviesRemotelyUseCase,
    featuredMoviesUpcomingCache,
    movieDao
) {
    override suspend fun getFeaturedMoviesFromDatabase(): List<FeaturedMovieUpcoming> {
        return movieDao.getFeaturedMoviesUpcoming()
    }

    override suspend fun removeOldMoviesFromDatabaseCache() {
        movieDao.deleteAllMoviesUpcoming()
    }

    override suspend fun insertNewMoviesInDatabaseCache(movies: List<FeaturedMovieUpcoming>) {
        movieDao.insertMoviesUpcoming(movies)
    }
}