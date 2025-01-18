package com.rng350.mediatracker.movies.usecases

import com.rng350.mediatracker.common.SaveImageFromURLUseCase
import com.rng350.mediatracker.movies.Movie
import com.rng350.mediatracker.networking.TMDBApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor(
    private val TMDBApi: TMDBApi,
    private val saveImageFromURLUseCase: SaveImageFromURLUseCase
) {
    private var lastApiRequestNano = 0L

    suspend fun fetchedSearchedMovies(query: String, releaseYear: String? = null, includeAdultContent: Boolean = false, page: Int): List<Movie> = withContext(Dispatchers.IO) {
        if (hasEnoughTimePassed()) {
            lastApiRequestNano = System.nanoTime()
            val searchedMovies = TMDBApi.searchMovies(query, releaseYear, includeAdultContent, page)?.movies?.map { fetchedMovie ->
                async {
                    val savedImageUri = saveImageFromURLUseCase(fetchedMovie.posterPath)
                    Movie(
                        movieId = fetchedMovie.id,
                        movieTitle = fetchedMovie.title,
                        movieReleaseDate = LocalDate.parse(fetchedMovie.releaseDate, DateTimeFormatter.ISO_LOCAL_DATE),
                        moviePremise = fetchedMovie.overview,
                        moviePosterUri = savedImageUri,
                        lastRefreshedDateTime = OffsetDateTime.now()
                    )
                }
            }?.awaitAll()
            searchedMovies ?: emptyList()
        } else {
            emptyList()
        }
    }

    private fun hasEnoughTimePassed(): Boolean {
        return System.nanoTime() - lastApiRequestNano > THROTTLE_TIMEOUT_MS * 1_000_000
    }

    companion object {
        private const val THROTTLE_TIMEOUT_MS = 250L
    }
}