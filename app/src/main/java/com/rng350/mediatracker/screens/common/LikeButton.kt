package com.rng350.mediatracker.screens.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rng350.mediatracker.R

@Composable
fun LikeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isActivated: Boolean
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(84.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(48.dp),
                imageVector = if (isActivated) Icons.Default.Favorite
                    else Icons.Default.FavoriteBorder,
                contentDescription = stringResource(R.string.like_icon),
                tint = if (isActivated) Color.Red else Color.LightGray
            )
            Text(
                text = if (isActivated) stringResource(R.string.like)
                else stringResource(R.string.liked),
                fontSize = 10.sp,
                color = Color.LightGray
            )
        }
    }
}

@Preview
@Composable
fun LikeButtonTest1() {
    LikeButton({}, isActivated = false)
}

@Preview
@Composable
fun LikeButtonTest2() {
    LikeButton({}, isActivated = true)
}