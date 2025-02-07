package com.rng350.mediatracker.screens.discovermovies

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rng350.mediatracker.common.decodeFromBase64
import com.rng350.mediatracker.movies.MovieForDisplay
import com.rng350.mediatracker.movies.usecases.SearchMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverMoviesViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _movieSearchResults = MutableStateFlow<List<MovieForDisplay>>(emptyList())
    val movieSearchResults: StateFlow<List<MovieForDisplay>> = _movieSearchResults.asStateFlow()

    init {
        val searchedQuery = savedStateHandle["searchQuery"] ?: ""
        newMovieSearch(searchedQuery.decodeFromBase64())
    }
    
    fun newMovieSearch(query: String) {
        viewModelScope.launch {
            try {
                _movieSearchResults.update {
                    searchMoviesUseCase.newMovieSearch(
                        query = query,
                        releaseYear = null,
                        includeAdultContent = false
                    )
                }
            } catch (e: CancellationException) {
                Log.e("DiscoverMoviesViewModel", "Coroutine cancelled: ${e.message}")
            } catch (e: Exception) {
                Log.e("DiscoverMoviesViewModel", "Exception: ${e.message}")
            }
        }
    }

    fun isLoading(): Boolean = searchMoviesUseCase.isLoading()

    fun onLastPage(): Boolean = searchMoviesUseCase.onLastPage()

    fun loadMore(query: String) {
        viewModelScope.launch {
            try {
                _movieSearchResults.update {
                    _movieSearchResults.value +
                            searchMoviesUseCase.fetchMoreSearchedMovies(
                                query = query,
                                releaseYear = null,
                                includeAdultContent = false
                            )
                }
            } catch (e: CancellationException) {
                Log.e("DiscoverMoviesViewModel", "Coroutine cancelled: ${e.message}")
            } catch (e: Exception) {
                Log.e("DiscoverMoviesViewModel", "Exception: ${e.message}")
            }
        }
    }
}