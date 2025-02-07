package com.rng350.mediatracker.featuredmovies.usecases

import com.rng350.mediatracker.common.Constants
import com.rng350.mediatracker.featuredmovies.FeaturedMovieTrendingThisWeek
import com.rng350.mediatracker.networking.TMDBApi
import com.rng350.mediatracker.networking.TmdbApiTimer
import java.time.LocalDate
import javax.inject.Inject

class FetchThisWeeksTrendingMoviesRemotelyUseCase @Inject constructor(
    override val tmdbApi: TMDBApi,
    override val tmdbApiTimer: TmdbApiTimer
): FetchFeaturedMoviesRemotelyUseCase<FeaturedMovieTrendingThisWeek>(tmdbApi, tmdbApiTimer) {
    override suspend fun fetchFeaturedMovies(): List<FeaturedMovieTrendingThisWeek> {
        return tmdbApi.getTrendingMoviesOfWeek(1)
            .body()
            ?.movies
            ?.mapIndexed { index, movieSchema ->
                FeaturedMovieTrendingThisWeek(
                    movieId = movieSchema.id.toInt(),
                    movieTitle = movieSchema.title,
                    moviePosterUrl = Constants.TMDB_IMAGE_BASE_URL + movieSchema.posterPath,
                    orderInList = index,
                    dateOfFeaturing = LocalDate.now()
                )
            } ?: emptyList()
    }
}