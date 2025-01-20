package com.rng350.mediatracker.screens.moviedetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MovieDetailsScreen(
    movieId: String,
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = movieId,
            fontSize = 30.sp
        )
    }
}