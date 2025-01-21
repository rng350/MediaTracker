package com.rng350.mediatracker.movies

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "movie_genre_table"
)
data class MovieGenreAssociation(
    @ColumnInfo("movie_id")
    val movieId: Int,
    @ColumnInfo("genre_id")
    val genreId: Int
)
