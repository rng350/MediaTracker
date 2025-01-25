package com.rng350.mediatracker.screens.common

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.rng350.mediatracker.R

@Composable
fun WatchedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isActivated: Boolean
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.visibility_24dp),
            contentDescription = stringResource(R.string.tracked_item_icon),
            tint = if (isActivated) Color.Green else Color.Unspecified
        )
    }
}
