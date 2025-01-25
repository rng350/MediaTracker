package com.rng350.mediatracker.movies.usecases

import com.rng350.mediatracker.common.Constants.TMDB_IMAGE_SUBFOLDER
import com.rng350.mediatracker.common.SaveImageFromURLUseCase
import com.rng350.mediatracker.common.database.MovieDao
import com.rng350.mediatracker.common.database.MovieGenreDao
import com.rng350.mediatracker.common.database.MovieStaffDao
import com.rng350.mediatracker.movies.MovieDetails
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddMovieToWatchlistUseCase @Inject constructor(
    private val saveImageFromUrl: SaveImageFromURLUseCase,
    private val movieDao: MovieDao,
    private val movieStaffDao: MovieStaffDao,
    private val movieGenreDao: MovieGenreDao
) {

    operator fun invoke(movieDetails: MovieDetails) {
        GlobalScope.launch {
            movieDetails.moviePosterUrl?.let { posterUrl ->
                saveImageFromUrl(posterUrl, TMDB_IMAGE_SUBFOLDER)
            }
        }
    }
}