package com.rng350.mediatracker.screens

import androidx.navigation.NavHostController
import com.rng350.mediatracker.Route
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ScreensNavigator {
    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)
    private lateinit var navController: NavHostController
    private var navControllerObserveJob: Job? = null
    val currentRoute = MutableStateFlow<Route?>(null)

    fun setNavController(setNavController: NavHostController) {
        navController = setNavController
        navControllerObserveJob?.cancel()
        navControllerObserveJob = coroutineScope.launch {
            navController.currentBackStackEntryFlow.map { backStackEntry ->
                val route = when (val routeName = backStackEntry.destination.route) {
                    Route.DiscoverMoviesScreen.routeName -> Route.DiscoverMoviesScreen
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

    fun navigateToRoute(route: Route) {
        navController.navigate(route.navCommand)
    }
}