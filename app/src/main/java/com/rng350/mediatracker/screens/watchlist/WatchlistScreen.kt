package com.rng350.mediatracker.screens.watchlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rng350.mediatracker.R
import com.rng350.mediatracker.common.toMonthAndYearDisplay
import com.rng350.mediatracker.screens.common.MediaTrackerSearchBar
import com.rng350.mediatracker.screens.common.MovieItemCard
import java.time.LocalDate
import java.time.Month

@Composable
fun WatchlistScreen(
    watchlistViewModel: WatchlistViewModel = hiltViewModel(),
    onMovieClicked: (String) -> Unit
) {
    Column {
        var query by rememberSaveable { mutableStateOf("") }
        MediaTrackerSearchBar(
            query = query,
            onQueryChanged = { newQuery -> query = newQuery },
            onSearch = {
                watchlistViewModel.searchWatchlist(query)
            },
            placeholderText = stringResource(R.string.search_movie_watchlist),
            modifier = Modifier
        )
        val movieList = watchlistViewModel.movieList.collectAsState()
        val listState = rememberLazyListState()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = listState,
            contentPadding = PaddingValues(all = 5.dp)
        ) {
            items(count = movieList.value.size) { index ->
                val currentMovie = movieList.value[index]
                val previousMovie = if (index-1 >= 0) movieList.value[index-1] else null

                if (index > 0) {
                    Spacer(Modifier.size(5.dp))
                }

                if (
                    index==0
                    && currentMovie.movieReleaseDate!=null
                    && currentMovie.movieReleaseDate.isBefore(LocalDate.now())
                ) {
                    MovieReleaseDateHeader(stringResource(R.string.out_now))
                }
                else if (
                    currentMovie.movieReleaseDate!=null
                    && currentMovie.movieReleaseDate.isAfter(LocalDate.now())
                ) {
                    val previousMovieNonexistantOrReleasedOnDifferentMonthAndYear = ((previousMovie==null)
                            || (currentMovie.movieReleaseDate.month != previousMovie.movieReleaseDate?.month)
                            || (currentMovie.movieReleaseDate.year != previousMovie.movieReleaseDate?.year))

                    if ((currentMovie.movieReleaseDate.year == LocalDate.now().year) && (currentMovie.movieReleaseDate.month == LocalDate.now().month)) {
                        if ((previousMovie==null) || previousMovie.movieReleaseDate?.isBefore(LocalDate.now())==true) {
                            MovieReleaseDateHeader(stringResource(R.string.later_this_month))
                        }
                    }
                    if ((((currentMovie.movieReleaseDate.year==LocalDate.now().year)
                                && ((currentMovie.movieReleaseDate.month.value-LocalDate.now().month.value)==1))
                        ||
                            ((currentMovie.movieReleaseDate.year-LocalDate.now().year==1)
                                && (currentMovie.movieReleaseDate.month==Month.JANUARY && LocalDate.now().month==Month.DECEMBER)))
                        && (previousMovieNonexistantOrReleasedOnDifferentMonthAndYear)) {
                        MovieReleaseDateHeader(stringResource(R.string.next_month))
                    }
                    else if (previousMovieNonexistantOrReleasedOnDifferentMonthAndYear) {
                        MovieReleaseDateHeader(currentMovie.movieReleaseDate.toMonthAndYearDisplay())
                    }
                }
                else if (currentMovie.movieReleaseDate == null) {
                    if ((previousMovie == null) || previousMovie.movieReleaseDate != null) {
                        MovieReleaseDateHeader(stringResource(R.string.no_release_date_header))
                    }
                }
                MovieItemCard(
                    movie = currentMovie,
                    modifier = Modifier,
                    onMovieClicked = { onMovieClicked(currentMovie.movieId.toString()) }
                )
            }
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
}

@Composable
fun MovieReleaseDateHeader(header: String) {
    Text(
        text = header,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.bodyLarge
    )
}