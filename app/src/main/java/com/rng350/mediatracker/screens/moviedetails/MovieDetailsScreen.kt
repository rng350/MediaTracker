package com.rng350.mediatracker.screens.moviedetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.rng350.mediatracker.R
import com.rng350.mediatracker.common.toDisplay
import com.rng350.mediatracker.screens.common.StaffRoleCard

@Composable
fun MovieDetailsScreen(
    movieId: String,
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    LaunchedEffect(movieId) {
        viewModel.fetchMovieDetails(movieId)
    }
    val movieDetailsResult = viewModel.movieDetails.collectAsState().value
    when (movieDetailsResult) {
        MovieDetailsViewModel.MovieDetailsResult.Error -> {
            Text(
                text = "Failed to fetch details...",
                modifier = Modifier.fillMaxWidth()
            )
        }
        MovieDetailsViewModel.MovieDetailsResult.None -> {
            Text(
                text = "Loading...",
                modifier = Modifier.fillMaxWidth()
            )
        }
        is MovieDetailsViewModel.MovieDetailsResult.Success -> {
            Column(horizontalAlignment = Alignment.Start) {
                val movieDetails = movieDetailsResult.movieDetails
                Row(
                    modifier = Modifier
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Top
                ) {
                    AsyncImage(
                        model = movieDetails.moviePosterUrl,
                        placeholder = painterResource(R.drawable.blank_poster),
                        contentDescription = stringResource(R.string.movie_poster),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(120.dp)
                            .height(180.dp)
                    )
                    Spacer(
                        Modifier.height(20.dp)
                    )
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Text(
                            text = movieDetails.movieTitle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            text = movieDetails.movieReleaseDate?.toDisplay() ?: stringResource(R.string.no_release_date)
                        )
                        val directorsList = movieDetails
                            .movieDirectors
                            .joinToString (
                                prefix = stringResource(R.string.directed_by),
                                separator = ", ",
                                limit = 5,
                                truncated = stringResource(R.string.et_al),
                                transform = { it.personName }
                            )
                        Text(
                            text = directorsList
                        )
                        Text(
                            text = stringResource(R.string.genres),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        val genresList = movieDetails
                            .movieGenres
                            .joinToString (
                                separator = ", ",
                                transform = { it.genreName }
                            )
                        Text(
                            text = genresList
                        )
                    }
                }
                Column {
                    Text(
                        text = stringResource(R.string.overview),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = movieDetails.movieOverview
                    )
                    Text(
                        text = stringResource(R.string.starring),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    val listState = rememberLazyListState()
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = listState
                    ) {
                        val actorsList = movieDetails.movieActors
                        items(count = actorsList.size) { index ->
                            if (index > 0) {
                                Spacer(modifier = Modifier.height(5.dp))
                            }
                            StaffRoleCard(
                                movieCharacter = actorsList[index],
                                onItemClicked = {}
                            )
                        }
                    }
                }
            }
        }
    }
}