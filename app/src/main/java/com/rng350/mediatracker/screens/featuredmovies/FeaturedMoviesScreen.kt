package com.rng350.mediatracker.screens.featuredmovies

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rng350.mediatracker.R
import com.rng350.mediatracker.screens.common.MediaTrackerSearchBar
import com.rng350.mediatracker.screens.featuredmovies.components.FailedToRetrieveMovies
import com.rng350.mediatracker.screens.featuredmovies.components.FeaturedMoviesList
import com.rng350.mediatracker.screens.featuredmovies.components.TrendingPeriod
import com.rng350.mediatracker.screens.featuredmovies.components.TrendingToggleButton

@Composable
fun FeaturedMoviesScreen(
    featuredMoviesViewModel: FeaturedMoviesViewModel = hiltViewModel(),
    onMovieClicked: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val textSize = 20.sp
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier.verticalScroll(state = scrollState)
    ) {
        var query by rememberSaveable { mutableStateOf("") }
        MediaTrackerSearchBar(
            query = query,
            onQueryChanged = {newQuery -> query = newQuery},
            onSearch = {
                onSearch(query)
            },
            placeholderText = stringResource(R.string.search_movies),
            modifier = Modifier
        )
        Text(
            text = stringResource(R.string.now_playing),
            fontWeight = FontWeight.Bold,
            fontSize = textSize
        )
        Row(
            modifier = Modifier.height(150.dp)
        ) {
            when(val results = featuredMoviesViewModel.moviesNowPlaying.collectAsState().value) {
                FeaturedMoviesViewModel.MoviesNowPlayingResult.Error -> {
                    FailedToRetrieveMovies()
                }
                FeaturedMoviesViewModel.MoviesNowPlayingResult.None -> {
                    Text(text = stringResource(R.string.loading))
                }
                is FeaturedMoviesViewModel.MoviesNowPlayingResult.Success -> {
                    FeaturedMoviesList(
                        movies = results.movies,
                        onMovieClicked = onMovieClicked,
                        listState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(R.string.upcoming),
            fontWeight = FontWeight.Bold,
            fontSize = textSize
        )
        Row(
            modifier = Modifier.height(150.dp)
        ) {
            when (val results = featuredMoviesViewModel.moviesUpcoming.collectAsState().value) {
                FeaturedMoviesViewModel.MoviesUpcomingResult.Error -> {
                    FailedToRetrieveMovies()
                }
                FeaturedMoviesViewModel.MoviesUpcomingResult.None -> {
                    Text(text = stringResource(R.string.loading))
                }
                is FeaturedMoviesViewModel.MoviesUpcomingResult.Success -> {
                    FeaturedMoviesList(
                        movies = results.movies,
                        onMovieClicked = onMovieClicked,
                        listState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        var trendingPeriodDisplayed by rememberSaveable{ mutableStateOf(TrendingPeriod.TODAY) }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.trending),
                fontWeight = FontWeight.Bold,
                fontSize = textSize
            )
            Spacer(Modifier.width(20.dp))
            TrendingToggleButton(
                selectedTrendingPeriod = trendingPeriodDisplayed,
                onToggle = { newTrendingPeriodDisplayed -> trendingPeriodDisplayed = newTrendingPeriodDisplayed }
            )
        }
        Row(
            modifier = Modifier.height(150.dp)
        ) {
            val moviesTrendingToday = featuredMoviesViewModel.moviesTrendingToday.collectAsState().value
            val moviesTrendingThisWeek = featuredMoviesViewModel.moviesTrendingThisWeek.collectAsState().value
            val moviesTrendingTodayState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }
            val moviesTrendingThisWeekState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }
            Crossfade(
                targetState = trendingPeriodDisplayed,
                label = "TrendingMoviesPeriodDisplayedCrossfade"
            ) { trendingPeriod ->
                when (trendingPeriod) {
                    TrendingPeriod.TODAY -> {
                        when (moviesTrendingToday) {
                            FeaturedMoviesViewModel.MoviesTrendingTodayResult.Error -> {
                                FailedToRetrieveMovies()
                            }
                            FeaturedMoviesViewModel.MoviesTrendingTodayResult.None -> {
                                Text(text = stringResource(R.string.loading))
                            }
                            is FeaturedMoviesViewModel.MoviesTrendingTodayResult.Success -> {
                                FeaturedMoviesList(
                                    movies = moviesTrendingToday.movies,
                                    onMovieClicked = onMovieClicked,
                                    listState = moviesTrendingTodayState
                                )
                            }
                        }
                    }
                    TrendingPeriod.THIS_WEEK -> {
                        when (moviesTrendingThisWeek) {
                            FeaturedMoviesViewModel.MoviesTrendingThisWeekResult.Error -> {
                                FailedToRetrieveMovies()
                            }
                            FeaturedMoviesViewModel.MoviesTrendingThisWeekResult.None -> {
                                Text(text = stringResource(R.string.loading))
                            }
                            is FeaturedMoviesViewModel.MoviesTrendingThisWeekResult.Success -> {
                                FeaturedMoviesList(
                                    movies = moviesTrendingThisWeek.movies,
                                    onMovieClicked = onMovieClicked,
                                    listState = moviesTrendingThisWeekState
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}