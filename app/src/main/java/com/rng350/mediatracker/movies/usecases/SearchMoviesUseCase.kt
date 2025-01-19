package com.rng350.mediatracker.movies.usecases

import android.util.Log
import com.rng350.mediatracker.common.Constants.TMDB_MAX_SEARCH_RESULTS_PER_PAGE
import com.rng350.mediatracker.movies.MovieForDisplay
import com.rng350.mediatracker.networking.TMDBApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class SearchMoviesUseCase @Inject constructor(
    private val theMovieDBAPI: TMDBApi
) {
    private var pageNumber = 1
    private var onLastPage = false
    private var isLoading = false
    private var lastApiRequestNano = 0L

    suspend fun fetchMoreSearchedMovies(
        query: String,
        releaseYear: String? = null,
        includeAdultContent: Boolean = false
    ): List<MovieForDisplay> = withContext(Dispatchers.IO) {
        try {
            isLoading = true
            val trimmedQuery = query.trim()
            if (trimmedQuery.isEmpty()) {
                isLoading = false
                return@withContext emptyList()
            }
            if (!onLastPage && hasEnoughTimePassed()) {
                lastApiRequestNano = System.nanoTime()
                Log.d("SearchMoviesUseCase", "1")
                val searchedMovies = theMovieDBAPI.searchMovies(
                    trimmedQuery,
                    /*releaseYear,*/
                    includeAdultContent,
                    pageNumber
                ).body()?.movies?.map { fetchedMovie ->
                    fetchedMovie.toMovieForDisplay()
                }
                if (!searchedMovies.isNullOrEmpty()) {
                    pageNumber++
                    if (searchedMovies.size < TMDB_MAX_SEARCH_RESULTS_PER_PAGE) {
                        onLastPage = true
                    }
                }
                isLoading = false
                return@withContext searchedMovies ?: emptyList()
            } else {
                isLoading = false
                return@withContext emptyList()
            }
        } catch (e: HttpException) {
            Log.e("API_ERROR", "HTTP error: ${e.code()}")
        } catch (e: IOException) {
            Log.e("API_ERROR", "Network error: ${e.localizedMessage}")
        } catch(e: CancellationException) {
            Log.e("API_ERROR", "CancellationException -- Parent coroutine was cancelled:. MESSAGE: ${e.message ?: "No message"}")
            throw e
        } catch (e: Exception) {
            Log.e("API_ERROR", "Unexpected error: ${e.localizedMessage}")
        }
        return@withContext emptyList()
    }

    suspend fun newMovieSearch(query: String, releaseYear: String? = null, includeAdultContent: Boolean = false): List<MovieForDisplay> {
        pageNumber = 1
        onLastPage = false
        isLoading = false
        return fetchMoreSearchedMovies(query, releaseYear, includeAdultContent)
    }

    fun isLoading(): Boolean = isLoading
    fun onLastPage(): Boolean = onLastPage

    private fun hasEnoughTimePassed(): Boolean {
        return System.nanoTime() - lastApiRequestNano > THROTTLE_TIMEOUT_MS * 1_000_000
    }

    companion object {
        private const val THROTTLE_TIMEOUT_MS = 250L
    }
}