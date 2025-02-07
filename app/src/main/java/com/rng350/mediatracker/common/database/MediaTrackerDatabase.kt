package com.rng350.mediatracker.common.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rng350.mediatracker.featuredmovies.FeaturedMovieNowPlaying
import com.rng350.mediatracker.featuredmovies.FeaturedMovieTrendingThisWeek
import com.rng350.mediatracker.featuredmovies.FeaturedMovieTrendingToday
import com.rng350.mediatracker.featuredmovies.FeaturedMovieUpcoming
import com.rng350.mediatracker.movies.LikedMovie
import com.rng350.mediatracker.movies.MovieGenre
import com.rng350.mediatracker.movies.Movie
import com.rng350.mediatracker.movies.MovieActingRole
import com.rng350.mediatracker.movies.MovieDirector
import com.rng350.mediatracker.movies.MovieGenreAssociation
import com.rng350.mediatracker.movies.MovieStaff
import com.rng350.mediatracker.movies.WatchedMovie
import com.rng350.mediatracker.movies.WatchlistedMovie

@Database(
    entities =
        [
            Movie::class,
            MovieStaff::class,
            MovieActingRole::class,
            MovieDirector::class,
            MovieGenre::class,
            MovieGenreAssociation::class,
            WatchedMovie::class,
            WatchlistedMovie::class,
            LikedMovie::class,
            FeaturedMovieNowPlaying::class,
            FeaturedMovieUpcoming::class,
            FeaturedMovieTrendingToday::class,
            FeaturedMovieTrendingThisWeek::class
        ],
    version = 3
)
@TypeConverters(value = [RoomDatabaseTypeConverters::class])
abstract class MediaTrackerDatabase : RoomDatabase() {
    abstract val movieDao: MovieDao
    abstract val movieStaffDao: MovieStaffDao
    abstract val movieGenreDao: MovieGenreDao
}