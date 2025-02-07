package com.rng350.mediatracker.featuredmovies.usecases

import com.rng350.mediatracker.common.database.MovieDao
import com.rng350.mediatracker.featuredmovies.FeaturedMovieTrendingToday
import com.rng350.mediatracker.featuredmovies.FeaturedMoviesTrendingTodayCache
import javax.inject.Inject

class FetchTodaysTrendingMoviesUseCase @Inject constructor(
    fetchMoviesTrendingTodayRemotely: FetchTodaysTrendingMoviesRemotelyUseCase,
    override val featuredMoviesCache: FeaturedMoviesTrendingTodayCache,
    override val movieDao: MovieDao
): FetchFeaturedMoviesUseCase<FeaturedMovieTrendingToday>(
    fetchMoviesTrendingTodayRemotely,
    featuredMoviesCache,
    movieDao
) {
    override suspend fun getFeaturedMoviesFromDatabase(): List<FeaturedMovieTrendingToday> {
        return movieDao.getMoviesTrendingToday()
    }

    override suspend fun removeOldMoviesFromDatabaseCache() {
        movieDao.deleteAllMoviesTrendingToday()
    }

    override suspend fun insertNewMoviesInDatabaseCache(movies: List<FeaturedMovieTrendingToday>) {
        movieDao.insertMoviesTrendingToday(movies)
    }
}