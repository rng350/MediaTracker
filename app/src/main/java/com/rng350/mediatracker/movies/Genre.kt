package com.rng350.mediatracker.movies

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.OffsetDateTime

@Entity(tableName = "genre_table")
data class Genre(
    @PrimaryKey
    @ColumnInfo("genre_id", index = true)
    val genreId: Int,
    @ColumnInfo("genre_name")
    val genreName: String,
    @ColumnInfo(name="last_refreshed_datetime")
    val lastRefreshedDateTime: OffsetDateTime
)
