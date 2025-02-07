package com.rng350.mediatracker.screens

import android.util.Log
import androidx.navigation.NavHostController
import com.rng350.mediatracker.BottomTab
import com.rng350.mediatracker.Route
import com.rng350.mediatracker.common.decodeFromBase64
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ScreensNavigator {
    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)
    private lateinit var parentNavController: NavHostController
    private lateinit var nestedNavController: NavHostController
    private var parentNavControllerObserveJob: Job? = null
    private var nestedNavControllerObserveJob: Job? = null
    val currentBottomTab = MutableStateFlow<BottomTab?>(null)
    val currentRoute = MutableStateFlow<Route?>(null)
    val isRootRoute = MutableStateFlow(false)


    fun setParentNavController(navHostController: NavHostController) {
        parentNavController = navHostController
        parentNavControllerObserveJob?.cancel()
        parentNavControllerObserveJob = coroutineScope.launch {
            navHostController.currentBackStackEntryFlow.map { backStackEntry ->
                val bottomTab = when (val routeName = backStackEntry.destination.route) {
                    Route.DiscoverMoviesScreen().routeName -> BottomTab.Discover
                    Route.MovieWatchlistScreen.routeName -> BottomTab.Watchlist
                    Route.WatchedMoviesListScreen.routeName -> BottomTab.Watched
                    null -> null
                    else -> throw RuntimeException("Invalid bottom tab! Route name: $routeName")
                }
                currentBottomTab.value = bottomTab
            }.collect()
        }
    }

    fun setNestedNavController(setNavController: NavHostController) {
        nestedNavController = setNavController
        nestedNavControllerObserveJob?.cancel()
        nestedNavControllerObserveJob = coroutineScope.launch {
            nestedNavController.currentBackStackEntryFlow.map { backStackEntry ->
                val route = when (val routeName = backStackEntry.destination.route) {
                    Route.FeaturedMoviesScreen.routeName -> Route.FeaturedMoviesScreen
                    Route.DiscoverMoviesScreen().routeName -> {
                        val args = backStackEntry.arguments
                        Route.DiscoverMoviesScreen(
                            args?.getString("searchQuery")!!.decodeFromBase64()
                        )
                    }
                    Route.MovieWatchlistScreen.routeName -> Route.MovieWatchlistScreen
                    Route.WatchedMoviesListScreen.routeName -> Route.WatchedMoviesListScreen
                    Route.MovieDetailsScreen().routeName -> {
                        val args = backStackEntry.arguments
                        Route.MovieDetailsScreen(
                            args?.getString("movieId")!!
                        )
                    }
                    null -> null
                    else -> throw RuntimeException("Invalid route: $routeName")
                }
                currentRoute.value = route
            }.collect()
        }
    }

    fun navigateBack() {
        if (!nestedNavController.popBackStack()) {
            parentNavController.popBackStack()
        }
    }

    fun navigateToTab(bottomTab: BottomTab) {
        val route = when(bottomTab) {
            BottomTab.Discover -> Route.DiscoverMoviesScreen()
            BottomTab.Watched -> Route.WatchedMoviesListScreen
            BottomTab.Watchlist -> Route.MovieWatchlistScreen
        }
        parentNavController.navigate(route.routeName) {
            parentNavController.graph.startDestinationRoute?.let { startRoute ->
                popUpTo(startRoute) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    fun navigateToRoute(route: Route) {
        nestedNavController.navigate(route.navCommand)
    }
}