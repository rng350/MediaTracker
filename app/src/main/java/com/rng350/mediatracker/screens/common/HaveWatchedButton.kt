package com.rng350.mediatracker.screens.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
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
fun WatchButton(
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
                imageVector = if (isActivated) ImageVector.vectorResource(R.drawable.visibility_24px_filled)
                else ImageVector.vectorResource(R.drawable.visibility_24px_unfilled),
                contentDescription = stringResource(R.string.tracked_item_icon),
                tint = if (isActivated) Color.Green else Color.LightGray
            )
            Text(
                text = if (isActivated) stringResource(R.string.have_watched)
                    else stringResource(R.string.havent_watched),
                fontSize = 10.sp,
                color = Color.LightGray
            )
        }
    }
}

@Preview
@Composable
fun WatchButtonTest() {
    WatchButton({}, isActivated = true)
}

@Preview
@Composable
fun WatchButtonTest2() {
    WatchButton({}, isActivated = false)
}