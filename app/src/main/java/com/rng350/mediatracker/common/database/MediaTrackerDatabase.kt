package com.rng350.mediatracker.common.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rng350.mediatracker.movies.Movie

@Database(
    entities = [Movie::class],
    version = 1
)
@TypeConverters(value = [RoomDatabaseTypeConverters::class])
abstract class MediaTrackerDatabase : RoomDatabase() {
    abstract val movieDao: MovieDao
}