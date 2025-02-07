package com.rng350.mediatracker.featuredmovies.usecases

import com.rng350.mediatracker.common.database.MovieDao
import com.rng350.mediatracker.featuredmovies.FeaturedMovieTrendingThisWeek
import com.rng350.mediatracker.featuredmovies.FeaturedMoviesTrendingThisWeekCache
import javax.inject.Inject

class FetchThisWeeksTrendingMoviesUseCase @Inject constructor(
    fetchThisWeeksTrendingMoviesRemotelyUseCase: FetchThisWeeksTrendingMoviesRemotelyUseCase,
    featuredMoviesTrendingThisWeekCache: FeaturedMoviesTrendingThisWeekCache,
    override val movieDao: MovieDao
): FetchFeaturedMoviesUseCase<FeaturedMovieTrendingThisWeek>(
    fetchThisWeeksTrendingMoviesRemotelyUseCase,
    featuredMoviesTrendingThisWeekCache,
    movieDao
) {
    override suspend fun getFeaturedMoviesFromDatabase(): List<FeaturedMovieTrendingThisWeek> {
        return movieDao.getMoviesTrendingThisWeek()
    }

    override suspend fun removeOldMoviesFromDatabaseCache() {
        movieDao.deleteAllMoviesTrendingThisWeek()
    }

    override suspend fun insertNewMoviesInDatabaseCache(movies: List<FeaturedMovieTrendingThisWeek>) {
        movieDao.insertMoviesTrendingThisWeek(movies)
    }
}