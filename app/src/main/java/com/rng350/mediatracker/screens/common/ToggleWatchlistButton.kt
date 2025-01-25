package com.rng350.mediatracker.screens.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rng350.mediatracker.R

@Composable
fun ToggleWatchlistButton(
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
            Box(modifier = Modifier.size(48.dp)) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxSize(),
                    imageVector = if (isActivated)
                        ImageVector.vectorResource(R.drawable.schedule_24px_filled)
                    else ImageVector.vectorResource(R.drawable.schedule_24px_unfilled),
                    contentDescription = stringResource(R.string.tracked_item_icon),
                    tint = if (isActivated) Color.Cyan
                    else Color.LightGray
                )
                Icon(
                    imageVector = if (isActivated) ImageVector.vectorResource(R.drawable.minus_circle_24x)
                    else Icons.Filled.AddCircle,
                    contentDescription = "",
                    tint = Color.LightGray,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(12.dp)
                        .offset(x = (-2).dp, y = (-2).dp)
                )
            }
            Spacer(
                Modifier.height(6.dp)
            )
            Text(
                text = stringResource(R.string.add_to_watchlist),
                fontSize = 10.sp,
                color = Color.LightGray
            )
        }
    }
}

@Preview
@Composable
fun WatchlistPreview() {
    ToggleWatchlistButton({}, isActivated = true)
}