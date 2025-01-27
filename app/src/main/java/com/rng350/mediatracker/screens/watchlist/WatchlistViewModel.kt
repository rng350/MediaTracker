package com.rng350.mediatracker.screens.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rng350.mediatracker.movies.MovieForDisplay
import com.rng350.mediatracker.movies.usecases.GetWatchlistedFilmsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val getWatchlistedFilmsUseCase: GetWatchlistedFilmsUseCase
): ViewModel() {
    val movieList: StateFlow<List<MovieForDisplay>> = getWatchlistedFilmsUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = listOf()
    )
    private var currentCollectJob: Job? = null

    fun getEntireWatchlist() {
        currentCollectJob?.cancel()
    }

    fun searchWatchlist(query: String) {
        currentCollectJob?.cancel()
    }
}