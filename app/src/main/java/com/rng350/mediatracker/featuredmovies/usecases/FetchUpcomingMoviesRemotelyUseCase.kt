package com.rng350.mediatracker.featuredmovies.usecases

import com.rng350.mediatracker.common.Constants
import com.rng350.mediatracker.featuredmovies.FeaturedMovieUpcoming
import com.rng350.mediatracker.networking.TMDBApi
import com.rng350.mediatracker.networking.TmdbApiTimer
import java.time.LocalDate
import javax.inject.Inject

class FetchUpcomingMoviesRemotelyUseCase @Inject constructor(
    override val tmdbApi: TMDBApi,
    override val tmdbApiTimer: TmdbApiTimer
): FetchFeaturedMoviesRemotelyUseCase<FeaturedMovieUpcoming>(tmdbApi, tmdbApiTimer) {
    override suspend fun fetchFeaturedMovies(): List<FeaturedMovieUpcoming> {
        return tmdbApi.getUpcomingMovies(1)
            .body()
            ?.movies
            ?.mapIndexed { index, movieSchema ->
                FeaturedMovieUpcoming(
                    movieId = movieSchema.id.toInt(),
                    movieTitle = movieSchema.title,
                    moviePosterUrl = Constants.TMDB_IMAGE_BASE_URL + movieSchema.posterPath,
                    orderInList = index,
                    dateOfFeaturing = LocalDate.now()
                )
            } ?: emptyList()
    }
}