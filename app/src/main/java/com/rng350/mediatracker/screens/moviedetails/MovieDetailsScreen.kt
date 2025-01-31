package com.rng350.mediatracker.screens.moviedetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.rng350.mediatracker.R
import com.rng350.mediatracker.common.toDisplay
import com.rng350.mediatracker.screens.common.LikeButton
import com.rng350.mediatracker.screens.common.StaffRoleCard
import com.rng350.mediatracker.screens.common.ToggleWatchlistButton
import com.rng350.mediatracker.screens.common.WatchButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    movieId: String,
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    LaunchedEffect(movieId) {
        viewModel.fetchMovieDetails(movieId)
    }
    when (val movieDetailsResult = viewModel.movieDetails.collectAsState().value) {
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
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                var bottomSheetShouldDisplay by rememberSaveable { mutableStateOf(false) }
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.Start
                ) {
                    val movieDetails = movieDetailsResult.movieDetails
                    Row(
                        modifier = Modifier
                            .padding(5.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier
                            .width(120.dp)
                            .height(200.dp)
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
                        }
                        Spacer(
                            Modifier.width(20.dp)
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
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center
                        )

                        val listState = rememberLazyListState()
                        LazyRow (
                            modifier = Modifier.fillMaxSize(),
                            state = listState
                        ) {
                            val actorsList = movieDetails.movieActors
                            items(count = actorsList.size) { index ->
                                if (index > 0) {
                                    Spacer(modifier = Modifier.width(3.dp))
                                }
                                StaffRoleCard(
                                    movieCharacter = actorsList[index],
                                    onItemClicked = {}
                                )
                            }
                        }
                    }

                    val bottomSheetState = rememberModalBottomSheetState()
                    if (bottomSheetShouldDisplay) {
                        ModalBottomSheet(
                            onDismissRequest = {
                                bottomSheetShouldDisplay = false
                            },
                            sheetState = bottomSheetState
                        ) {
                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                Column {
                                    Text(
                                        text = movieDetails.movieTitle,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 20.sp
                                    )
                                    movieDetails.movieReleaseDate?.let { releaseDate ->
                                        Text(
                                            text = releaseDate.year.toString(),
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(
                                        Modifier.height(10.dp)
                                    )
                                    HorizontalDivider()
                                    Spacer(
                                        Modifier.height(10.dp)
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceAround
                                    ) {
                                        val movieWatchedIconSate = viewModel.movieHasBeenWatched.collectAsState()
                                        WatchButton(
                                            onClick = { viewModel.toggleWatchedMovie() },
                                            isActivated = movieWatchedIconSate.value
                                        )
                                        val movieIsLikedIconState = viewModel.movieIsLiked.collectAsState()
                                        LikeButton(
                                            onClick = { viewModel.toggleLikeMovie() },
                                            isActivated = movieIsLikedIconState.value
                                        )
                                        val movieIsOnWatchlistIconState = viewModel.movieIsOnWatchlist.collectAsState()
                                        ToggleWatchlistButton(
                                            onClick = { viewModel.toggleToWatchList() },
                                            isActivated = movieIsOnWatchlistIconState.value
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                FloatingActionButton(
                    onClick = { bottomSheetShouldDisplay = !bottomSheetShouldDisplay },
                    shape = CircleShape,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x=(-10).dp, y=(-10).dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add_movie),
                        modifier = Modifier
                    )
                }
            }
        }
    }
}