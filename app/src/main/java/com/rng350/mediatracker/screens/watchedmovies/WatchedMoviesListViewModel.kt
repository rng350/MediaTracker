package com.rng350.mediatracker.screens.watchedmovies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rng350.mediatracker.movies.MovieForDisplay
import com.rng350.mediatracker.movies.usecases.GetWatchedFilmsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class WatchedMoviesListViewModel @Inject constructor(
    getWatchedFilmsUseCase: GetWatchedFilmsUseCase
): ViewModel() {
    val movies: StateFlow<List<MovieForDisplay>> = getWatchedFilmsUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = listOf()
    )

    private var currentCollectJob: Job? = null

    fun getEntireList() {
        currentCollectJob?.cancel()
    }

    fun searchList(query: String) {
        currentCollectJob?.cancel()
    }
}