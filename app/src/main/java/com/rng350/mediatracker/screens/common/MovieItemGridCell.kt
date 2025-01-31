package com.rng350.mediatracker.screens.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.rng350.mediatracker.R
import com.rng350.mediatracker.movies.MovieForDisplay

@Composable
fun MovieItemGridCell(
    movie: MovieForDisplay,
    modifier: Modifier = Modifier,
    onMovieClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.66666666f)
            .padding(2.dp),
        shape = RectangleShape,
        onClick = { onMovieClicked() }
    ) {
        if (movie.moviePosterUri!=null || movie.moviePosterUrl!=null) {
            Box(
                modifier = Modifier
            ) {
                var moviePosterFailedToLoad by rememberSaveable { mutableStateOf(false) }
                AsyncImage(
                    model = movie.moviePosterUri ?: movie.moviePosterUrl,
                    fallback = painterResource(R.drawable.blank_poster),
                    placeholder = painterResource(R.drawable.blank_poster),
                    contentDescription = stringResource(R.string.movie_poster),
                    onError = { moviePosterFailedToLoad = true },
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(1f)
                )
                if (moviePosterFailedToLoad) {
                    Text(
                        text = movie.movieTitle,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .zIndex(2f),
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        maxLines = 3
                    )
                }
            }
        }
        else {
            Box(
                modifier = Modifier
            ) {
                AsyncImage(
                    model = null,
                    placeholder = painterResource(R.drawable.blank_poster),
                    contentDescription = stringResource(R.string.movie_poster),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp)
                        .zIndex(1f)
                )
                Text(
                    text = movie.movieTitle,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .zIndex(2f),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    maxLines = 3
                )
            }
        }
    }
}