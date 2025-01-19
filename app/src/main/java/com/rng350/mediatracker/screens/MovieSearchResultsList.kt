package com.rng350.mediatracker.screens

import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rng350.mediatracker.R
import com.rng350.mediatracker.movies.Movie
import com.rng350.mediatracker.movies.MovieForDisplay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import java.time.LocalDate
import java.time.OffsetDateTime

@Composable
fun MovieSearchResultsList(
    list: StateFlow<List<MovieForDisplay>>,
    isLoading: () -> Boolean,
    onLastPage: () -> Boolean,
    loadMore: () -> Unit
) {
    val listItems = list.collectAsState()
    val listState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(all = 5.dp)
    ) {
        items(count = listItems.value.size) { index ->
            val listItem = listItems.value[index]
            if (index > 0) {
                Spacer(Modifier.size(5.dp))
            }
            MovieItemCard(
                movie = listItem,
                modifier = Modifier
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
                if (lastVisibleItemIndex >= list.value.size-1 && !isLoading() && !onLastPage()) {
                    loadMore()
                }
            }
    }
}