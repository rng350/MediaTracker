package com.rng350.mediatracker.screens.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.rng350.mediatracker.R
import com.rng350.mediatracker.movies.MovieActorAndRolesInFilm

@Composable
fun StaffRoleCard(
    movieCharacter: MovieActorAndRolesInFilm,
    modifier: Modifier = Modifier,
    onItemClicked: () -> Unit
) {
    Column(
        modifier = Modifier.width(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = movieCharacter.personProfilePicUri ?: movieCharacter.personProfilePicUrl,
            placeholder = painterResource(R.drawable.blank_poster),
            contentDescription = stringResource(R.string.person_profile_picture),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Gray, CircleShape)
        )
        Text(
            text = movieCharacter.personName,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = movieCharacter.personRoles.joinToString { it.characterName },
            fontSize = 10.sp,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}