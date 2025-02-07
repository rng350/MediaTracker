package com.rng350.mediatracker.featuredmovies

import com.rng350.mediatracker.movies.MovieForDisplay
import java.time.LocalDate

abstract class FeaturedMovie(
    open val movieId: Int,
    open val movieTitle: String,
    open val moviePosterUrl: String?,
    open val orderInList: Int,
    open val dateOfFeaturing: LocalDate
) {
    fun toMovieForDisplay(): MovieForDisplay {
        return MovieForDisplay(
            movieId = movieId,
            movieTitle = movieTitle,
            movieOriginalTitle = null,
            movieReleaseDate = null,
            moviePremise = "",
            moviePosterUrl = moviePosterUrl,
            moviePosterUri = null
        )
    }
}
