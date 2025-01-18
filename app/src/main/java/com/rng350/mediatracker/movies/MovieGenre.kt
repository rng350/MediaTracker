package com.rng350.mediatracker.movies

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.time.OffsetDateTime

@Entity(
    tableName = "movie_genre_table"
)
data class MovieGenre(
    @ColumnInfo("movie_id")
    val movieId: Int,
    @ColumnInfo("genre_id")
    val genreId: Int,
    @ColumnInfo(name="last_refreshed_datetime")
    val lastRefreshedDateTime: OffsetDateTime
)
