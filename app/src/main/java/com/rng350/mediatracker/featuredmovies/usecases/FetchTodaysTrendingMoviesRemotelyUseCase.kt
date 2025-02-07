package com.rng350.mediatracker.featuredmovies.usecases

import com.rng350.mediatracker.common.Constants
import com.rng350.mediatracker.featuredmovies.FeaturedMovieTrendingToday
import com.rng350.mediatracker.networking.TMDBApi
import com.rng350.mediatracker.networking.TmdbApiTimer
import java.time.LocalDate
import javax.inject.Inject

class FetchTodaysTrendingMoviesRemotelyUseCase @Inject constructor(
    override val tmdbApi: TMDBApi,
    override val tmdbApiTimer: TmdbApiTimer
): FetchFeaturedMoviesRemotelyUseCase<FeaturedMovieTrendingToday>(tmdbApi, tmdbApiTimer) {
    override suspend fun fetchFeaturedMovies(): List<FeaturedMovieTrendingToday> {
        return tmdbApi.getTrendingMoviesOfDay(1)
            .body()
            ?.movies
            ?.mapIndexed { index, movieSchema ->
                FeaturedMovieTrendingToday(
                    movieId = movieSchema.id.toInt(),
                    movieTitle = movieSchema.title,
                    moviePosterUrl = Constants.TMDB_IMAGE_BASE_URL + movieSchema.posterPath,
                    orderInList = index,
                    dateOfFeaturing = LocalDate.now()
                )
            } ?: emptyList()
    }
}