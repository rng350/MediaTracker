package com.rng350.mediatracker.screens.watchedmovies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rng350.mediatracker.R
import com.rng350.mediatracker.screens.common.MediaTrackerSearchBar
import com.rng350.mediatracker.screens.common.MovieItemGridCell

@Composable
fun WatchedMoviesListScreen(
    watchedMoviesViewModel: WatchedMoviesListViewModel = hiltViewModel(),
    onMovieClicked: (String) -> Unit
) {
    Column {
        var query by rememberSaveable { mutableStateOf("") }
        MediaTrackerSearchBar(
            query = query,
            onQueryChanged = { newQuery -> query = newQuery },
            onSearch = {
                //watchlistViewModel.searchWatchlist(query)
            },
            placeholderText = stringResource(R.string.search_watched_movies),
            modifier = Modifier
        )
        val movieList = watchedMoviesViewModel.movies.collectAsState()
        val lazyGridState = rememberLazyGridState()

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 80.dp),
            modifier = Modifier,
            state = lazyGridState
        ) {
            items(count = movieList.value.size) { index ->
                val currentMovie = movieList.value[index]
                MovieItemGridCell(
                    movie = currentMovie,
                    onMovieClicked = { onMovieClicked(currentMovie.movieId.toString()) }
                )
            }
        }
    }
}