package com.rng350.mediatracker.common.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1
)
abstract class MediaTrackerDatabase : RoomDatabase() {
    abstract val movieDao: MovieDao
}