package com.rng350.mediatracker.screens.featuredmovies.components

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.rng350.mediatracker.R

@Composable
fun FailedToRetrieveMovies() {
    Icon(
        modifier = Modifier,
        imageVector = ImageVector.vectorResource(R.drawable.error_24px),
        contentDescription = stringResource(R.string.error_icon)
    )
    Text(
        text = stringResource(R.string.could_not_retrieve_movies)
    )
}