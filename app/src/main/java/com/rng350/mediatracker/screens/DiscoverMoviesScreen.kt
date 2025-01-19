package com.rng350.mediatracker.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.rng350.mediatracker.R
import com.rng350.mediatracker.screens.common.MediaTrackerSearchBar
import com.rng350.mediatracker.screens.main.MediaTrackerTopAppBar

@Composable
fun DiscoverMoviesScreen(
    discoverMoviesViewModel: DiscoverMoviesViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = { MediaTrackerTopAppBar() },
        content = { paddingValues ->
            Column(Modifier.padding(paddingValues)) {

                val searchQueryHasBeenEntered = rememberSaveable { mutableStateOf(false) }
                var query by rememberSaveable { mutableStateOf("") }

                MediaTrackerSearchBar(
                    query = query,
                    onQueryChanged = {newQuery -> query = newQuery},
                    onSearch = {
                        searchQueryHasBeenEntered.value = true
                        discoverMoviesViewModel.newMovieSearch(query)
                    },
                    placeholderText = stringResource(R.string.search_movies),
                    modifier = Modifier
                )
                if (searchQueryHasBeenEntered.value) {
                    MovieSearchResultsList(
                        list = discoverMoviesViewModel.movieSearchResults,
                        isLoading = { discoverMoviesViewModel.isLoading() },
                        onLastPage = { discoverMoviesViewModel.onLastPage() },
                        loadMore = { discoverMoviesViewModel.loadMore(query) }
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.search_for_movies),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun DiscoverMoviesPreview() {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = { MediaTrackerTopAppBar() },
        content = { paddingValues ->
            Column(Modifier.padding(paddingValues)) {
                val searchQueryHasBeenEntered = rememberSaveable { mutableStateOf(false) }
                MediaTrackerSearchBar(
                    query = "ysduashduiashduias",
                    onQueryChanged = {},
                    onSearch = {},
                    placeholderText = stringResource(R.string.search_movies),
                    modifier = Modifier
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.search_for_movies),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    )
}