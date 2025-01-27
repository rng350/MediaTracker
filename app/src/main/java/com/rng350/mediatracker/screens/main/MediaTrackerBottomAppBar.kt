package com.rng350.mediatracker.screens.main

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.rng350.mediatracker.BottomTab
import com.rng350.mediatracker.R
import kotlinx.coroutines.flow.StateFlow

@Composable
fun MediaTrackerBottomAppBar(
    activeTab: StateFlow<BottomTab?>,
    onTabClicked: (BottomTab) -> Unit
) {
    NavigationBar {
        val currentTab = activeTab.collectAsState()
        NavigationBarItem(
            selected = currentTab.value == BottomTab.Discover,
            onClick = { onTabClicked(BottomTab.Discover) },
            icon = { Icon(imageVector = ImageVector.vectorResource(R.drawable.internet_24px), contentDescription = "Discover") },
            label = { stringResource(R.string.discover) },
            alwaysShowLabel = true
        )
        NavigationBarItem(
            selected = currentTab.value == BottomTab.Watchlist,
            onClick = { onTabClicked(BottomTab.Watchlist) },
            icon = { Icon(imageVector = ImageVector.vectorResource(R.drawable.schedule_24px_unfilled), contentDescription = "Watchlist") },
            label = { stringResource(R.string.movie_watchlist) },
            alwaysShowLabel = true
        )
        NavigationBarItem(
            selected = currentTab.value == BottomTab.Watched,
            onClick = { onTabClicked(BottomTab.Watched) },
            icon = { Icon(imageVector = ImageVector.vectorResource(R.drawable.visibility_24px_unfilled), contentDescription = "Watched") },
            label = { stringResource(R.string.watched_movies) },
            alwaysShowLabel = true
        )
    }
}