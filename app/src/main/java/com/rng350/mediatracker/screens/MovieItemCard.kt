package com.rng350.mediatracker.screens

import android.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.rng350.mediatracker.R
import com.rng350.mediatracker.common.toDisplay
import com.rng350.mediatracker.movies.MovieForDisplay
import java.time.LocalDate

@Composable
fun MovieItemCard(movie: MovieForDisplay, modifier: Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp),
        onClick = {},
        shape = RoundedCornerShape(5.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            AsyncImage(
                model = movie.moviePosterUrl,
                placeholder = painterResource(R.drawable.blank_poster),
                contentDescription = stringResource(R.string.movie_poster),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(100.dp)
                    .height(150.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1.0f)
            ) {
                Text(
                    text = movie.movieTitle,
                    fontWeight = FontWeight.Bold
                )
                Text(text = movie.movieReleaseDate?.toDisplay() ?: stringResource(R.string.no_release_date))
                Spacer(Modifier.height(20.dp))
                Text(
                    text = movie.moviePremise,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = Color.MAGENTA.toLong()
)
@Composable
fun MovieItemPreview() {
    MovieItemCard(
        movie = MovieForDisplay(
            movieId = 123,
            movieTitle = "The Godfather",
            movieReleaseDate = LocalDate.of(1972, 3, 24),
            moviePremise = "Spanning the years 1945 to 1955, a chronicle of the fictional Italian-American Corleone crime family. When organized crime family patriarch, Vito Corleone barely survives an attempt on his life, his youngest son, Michael steps in to take care of the would-be killers, launching a campaign of bloody revenge.",
            moviePosterUrl = "https://cdn.myanimelist.net/images/anime/1/584l.jpg",
        ),
        Modifier
    )
}