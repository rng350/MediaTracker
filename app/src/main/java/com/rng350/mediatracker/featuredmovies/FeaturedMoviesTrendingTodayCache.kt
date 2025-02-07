package com.rng350.mediatracker.featuredmovies

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeaturedMoviesTrendingTodayCache @Inject constructor(): FeaturedMoviesCache<FeaturedMovieTrendingToday>()