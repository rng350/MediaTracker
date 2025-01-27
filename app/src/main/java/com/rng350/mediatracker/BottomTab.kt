package com.rng350.mediatracker

sealed class BottomTab(val title: String) {
    data object Discover: BottomTab("Discover")
    data object Watchlist: BottomTab("Watchlist")
    data object Watched: BottomTab("Watched")
}