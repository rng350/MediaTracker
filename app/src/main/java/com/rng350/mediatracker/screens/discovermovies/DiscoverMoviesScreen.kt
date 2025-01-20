package com.rng350.mediatracker.screens.discovermovies

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rng350.mediatracker.R
import com.rng350.mediatracker.screens.common.MediaTrackerSearchBar
import com.rng350.mediatracker.screens.common.MovieItemCard
import kotlinx.coroutines.flow.filterNotNull

@Composable
fun DiscoverMoviesScreen(
    discoverMoviesViewModel: DiscoverMoviesViewModel = hiltViewModel(),
    onMovieClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
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
            val movieList = discoverMoviesViewModel.movieSearchResults
            val isLoading = { discoverMoviesViewModel.isLoading() }
            val onLastPage = { discoverMoviesViewModel.onLastPage() }
            val loadMore = { discoverMoviesViewModel.loadMore(query) }
            val listItems = movieList.collectAsState()
            val listState = rememberLazyListState()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                state = listState,
                contentPadding = PaddingValues(all = 5.dp)
            ) {
                items(count = listItems.value.size) { index ->
                    val movie = listItems.value[index]
                    if (index > 0) {
                        Spacer(Modifier.size(5.dp))
                    }
                    MovieItemCard(
                        movie = movie,
                        modifier = Modifier,
                        onMovieClicked = { onMovieClicked(movie.movieId) }
                    )
                }
                if (isLoading()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(16.dp)
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
                if (onLastPage()) {
                    item {
                        Text(
                            text = stringResource(R.string.no_more_movies_to_load),
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            LaunchedEffect(key1 = listState) {
                snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                    .filterNotNull()
                    .collect { lastVisibleItemIndex ->
                        if (lastVisibleItemIndex >= movieList.value.size-1 && !isLoading() && !onLastPage()) {
                            loadMore()
                        }
                    }
            }
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