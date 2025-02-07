package com.rng350.mediatracker.screens.featuredmovies.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rng350.mediatracker.movies.MovieForDisplay
import com.rng350.mediatracker.screens.common.MovieItemGridCell
import com.rng350.mediatracker.screens.common.Parent

@Composable
fun FeaturedMoviesList(
    movies: List<MovieForDisplay>,
    onMovieClicked: (String) -> Unit
) {
    val movieList = movies
    LazyRow(modifier = Modifier.height(150.dp)) {
        items(count = movieList.size) { index ->
            MovieItemGridCell(
                movie = movieList[index],
                modifier = Modifier,
                onMovieClicked = { onMovieClicked(movieList[index].movieId.toString()) },
                parent = Parent.LazyGridCell
            )
        }
    }

}