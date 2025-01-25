package com.rng350.mediatracker.movies

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_genre_table")
data class MovieGenre(
    @PrimaryKey
    @ColumnInfo("genre_id", index = true)
    val genreId: Int,
    @ColumnInfo("genre_name", index = true)
    val genreName: String
)
