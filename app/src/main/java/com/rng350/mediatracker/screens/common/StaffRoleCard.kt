package com.rng350.mediatracker.screens.common

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.rng350.mediatracker.R
import com.rng350.mediatracker.movies.MovieCharacter

@Composable
fun StaffRoleCard(
    movieCharacter: MovieCharacter,
    modifier: Modifier = Modifier,
    onItemClicked: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp),
        onClick = {
            onItemClicked()
        },
        shape = RoundedCornerShape(5.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            AsyncImage(
                model = movieCharacter.personProfilePicUri ?: movieCharacter.personProfilePicUrl,
                placeholder = painterResource(R.drawable.blank_poster),
                contentDescription = stringResource(R.string.person_profile_picture),
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
                    text = movieCharacter.personName,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(20.dp))
                Text(
                    text = movieCharacter.personRoles.joinToString(),
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}