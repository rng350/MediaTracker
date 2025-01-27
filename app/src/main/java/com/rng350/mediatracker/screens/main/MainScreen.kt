package com.rng350.mediatracker.screens.main

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rng350.mediatracker.Route
import com.rng350.mediatracker.screens.discovermovies.DiscoverMoviesScreen
import com.rng350.mediatracker.screens.ScreensNavigator
import com.rng350.mediatracker.screens.moviedetails.MovieDetailsScreen
import com.rng350.mediatracker.screens.watchedmovies.WatchedMoviesListScreen
import com.rng350.mediatracker.screens.watchlist.WatchlistScreen

@Composable
fun MainScreen() {
    val screenNavigator = ScreensNavigator()
    val currentBottomTab = screenNavigator.currentBottomTab
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            MediaTrackerTopAppBar()
        },
        bottomBar = {
            MediaTrackerBottomAppBar(
                activeTab = currentBottomTab,
                onTabClicked = { bottomTab -> screenNavigator.navigateToTab(bottomTab) }
            )
        }
    ) { paddingValues ->
        val parentNavController = rememberNavController()
        screenNavigator.setParentNavController(parentNavController)

        Surface(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            NavHost(
                modifier = Modifier.fillMaxSize(),
                navController = parentNavController,
                startDestination = Route.DiscoverMoviesScreen.routeName,
                enterTransition = { fadeIn(animationSpec = tween(100)) },
                exitTransition = { fadeOut(animationSpec = tween(100)) }
            ) {
                composable(route = Route.DiscoverMoviesScreen.routeName) {
                    val discoverScreenNestedNavController = rememberNavController()
                    screenNavigator.setNestedNavController(discoverScreenNestedNavController)
                    NavHost(
                        navController = discoverScreenNestedNavController,
                        startDestination = Route.DiscoverMoviesScreen.routeName
                    ) {
                        composable(route = Route.DiscoverMoviesScreen.routeName) {
                            DiscoverMoviesScreen(onMovieClicked = { movieId ->
                                screenNavigator.navigateToRoute(Route.MovieDetailsScreen(movieId))
                            })
                        }
                        composable(route = Route.MovieDetailsScreen().routeName) {
                            val movieId = remember {
                                (screenNavigator.currentRoute.value as Route.MovieDetailsScreen).movieId
                            }
                            MovieDetailsScreen(
                                movieId = movieId
                            )
                        }
                    }
                }
                composable(route = Route.MovieWatchlistScreen.routeName) {
                    val movieWatchlistNestedNavController = rememberNavController()
                    screenNavigator.setNestedNavController(movieWatchlistNestedNavController)
                    NavHost(
                        navController = movieWatchlistNestedNavController,
                        startDestination = Route.MovieWatchlistScreen.routeName
                    ) {
                        composable(route = Route.MovieWatchlistScreen.routeName) {
                            WatchlistScreen( onMovieClicked = { movieId ->
                                screenNavigator.navigateToRoute(Route.MovieDetailsScreen(movieId))
                            })
                        }
                        composable(route = Route.MovieDetailsScreen().routeName) {
                            val movieId = remember {
                                (screenNavigator.currentRoute.value as Route.MovieDetailsScreen).movieId
                            }
                            MovieDetailsScreen(
                                movieId = movieId
                            )
                        }
                    }
                }
                composable(route = Route.WatchedMoviesListScreen.routeName) {
                    val watchedMoviesListNestedNavController = rememberNavController()
                    screenNavigator.setNestedNavController(watchedMoviesListNestedNavController)
                    NavHost(
                        navController = watchedMoviesListNestedNavController,
                        startDestination = Route.WatchedMoviesListScreen.routeName
                    ) {
                        composable(route = Route.WatchedMoviesListScreen.routeName) {
                            WatchedMoviesListScreen()
                        }
                        composable(route = Route.MovieDetailsScreen().routeName) {
                            val movieId = remember {
                                (screenNavigator.currentRoute.value as Route.MovieDetailsScreen).movieId
                            }
                            MovieDetailsScreen(
                                movieId = movieId
                            )
                        }
                    }
                }
            }
        }
    }
}
