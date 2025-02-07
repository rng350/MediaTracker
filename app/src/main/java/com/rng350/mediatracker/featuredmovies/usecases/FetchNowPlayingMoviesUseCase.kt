package com.rng350.mediatracker.featuredmovies.usecases

import com.rng350.mediatracker.common.database.MovieDao
import com.rng350.mediatracker.featuredmovies.FeaturedMovieNowPlaying
import com.rng350.mediatracker.featuredmovies.FeaturedMoviesNowPlayingCache
import javax.inject.Inject

class FetchNowPlayingMoviesUseCase @Inject constructor(
    fetchNowPlayingMoviesRemotelyUseCase: FetchNowPlayingMoviesRemotelyUseCase,
    featuredMoviesNowPlayingCache: FeaturedMoviesNowPlayingCache,
    override val movieDao: MovieDao
): FetchFeaturedMoviesUseCase<FeaturedMovieNowPlaying>(
    fetchNowPlayingMoviesRemotelyUseCase,
    featuredMoviesNowPlayingCache,
    movieDao
) {
    override suspend fun getFeaturedMoviesFromDatabase(): List<FeaturedMovieNowPlaying> {
        return movieDao.getMoviesNowPlaying()
    }

    override suspend fun removeOldMoviesFromDatabaseCache() {
        movieDao.deleteAllMoviesNowPlaying()
    }

    override suspend fun insertNewMoviesInDatabaseCache(movies: List<FeaturedMovieNowPlaying>) {
        movieDao.insertMoviesNowPlaying(movies)
    }
}