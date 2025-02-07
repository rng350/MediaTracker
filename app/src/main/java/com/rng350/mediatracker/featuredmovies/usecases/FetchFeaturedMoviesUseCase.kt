package com.rng350.mediatracker.featuredmovies.usecases

import com.rng350.mediatracker.common.database.MovieDao
import com.rng350.mediatracker.featuredmovies.FeaturedMovie
import com.rng350.mediatracker.featuredmovies.FeaturedMoviesCache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate

abstract class FetchFeaturedMoviesUseCase<T: FeaturedMovie>(
    open val fetchFeaturedMoviesRemotelyUseCase: FetchFeaturedMoviesRemotelyUseCase<T>,
    open val featuredMoviesCache: FeaturedMoviesCache<T>,
    open val movieDao: MovieDao
) {
    operator fun invoke(): Flow<FeaturedMoviesResult<T>> = flow {
        val featuredMoviesListCache = featuredMoviesCache.getFeaturedMovies()
        if (featuredMoviesListCache==null) {
            fetchFromDatabaseCache { emit(it) }
            return@flow
        } else {
            emit(FeaturedMoviesResult.Success(featuredMoviesListCache))
            if (featuredMoviesListCache.firstOrNull()?.dateOfFeaturing?.isBefore(LocalDate.now()) != false) {
                attemptToFetchFromRemote{ emit(it) }
            }
        }
    }

    private suspend fun fetchFromDatabaseCache(emitData: suspend (FeaturedMoviesResult<T>) -> Unit) {
        val featuredMoviesListFromDb = getFeaturedMoviesFromDatabase()
        if (featuredMoviesListFromDb.isEmpty()) {
            fetchFromRemoteAndReturnErrorIfFail(emitData)
        } else {
            featuredMoviesCache.updateCache(featuredMoviesListFromDb)
            emitData(FeaturedMoviesResult.Success(featuredMoviesListFromDb))
            if (featuredMoviesListFromDb.firstOrNull()?.dateOfFeaturing?.isBefore(LocalDate.now()) != false) {
                attemptToFetchFromRemote(emitData)
            }
        }
        return
    }

    private suspend fun attemptToFetchFromRemote(emitData: suspend (FeaturedMoviesResult<T>) -> Unit) {
        val nowPlayingListFromRemote = fetchFeaturedMoviesRemotelyUseCase()
        if (nowPlayingListFromRemote.isNotEmpty()) {
            emitData(FeaturedMoviesResult.Success(nowPlayingListFromRemote))
            updateDatabaseAndInMemoryCache(nowPlayingListFromRemote)
        }
    }

    private suspend fun fetchFromRemoteAndReturnErrorIfFail(emitData: suspend (FeaturedMoviesResult<T>) -> Unit) {
        val nowPlayingListFromRemote = fetchFeaturedMoviesRemotelyUseCase()
        if (nowPlayingListFromRemote.isNotEmpty()) {
            emitData(FeaturedMoviesResult.Success(nowPlayingListFromRemote))
            updateDatabaseAndInMemoryCache(nowPlayingListFromRemote)
            return
        }
        emitData(FeaturedMoviesResult.Error())
    }

    private suspend fun updateDatabaseAndInMemoryCache(movies: List<T>) {
        featuredMoviesCache.updateCache(movies)
        removeOldMoviesFromDatabaseCache()
        insertNewMoviesInDatabaseCache(movies)
    }

    // database calls go in these functions
    abstract suspend fun getFeaturedMoviesFromDatabase(): List<T>
    abstract suspend fun removeOldMoviesFromDatabaseCache()
    abstract suspend fun insertNewMoviesInDatabaseCache(movies: List<T>)

    sealed class FeaturedMoviesResult<T> {
        data class Success<T>(val movies: List<T>): FeaturedMoviesResult<T>()
        class Error<T> : FeaturedMoviesResult<T>()
    }
}