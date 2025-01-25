package com.rng350.mediatracker.movies

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "watched_movie_table",
    foreignKeys = [
        ForeignKey(
            entity = Movie::class,
            parentColumns = ["movie_id"],
            childColumns = ["movie_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class WatchedMovie(
    @PrimaryKey
    @ColumnInfo(name="movie_id", index = true)
    val movieId: Int
)
