package com.rng350.mediatracker.movies

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "movie_genre_association_table",
    primaryKeys = ["movie_id", "genre_id"],
    foreignKeys = [
        ForeignKey(
            entity = Movie::class,
            parentColumns = ["movie_id"],
            childColumns = ["movie_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["movie_id", "genre_id"]),
        Index(value = ["genre_id", "movie_id"])
    ]
)
data class MovieGenreAssociation(
    @ColumnInfo("movie_id")
    val movieId: Int,
    @ColumnInfo("genre_id")
    val genreId: Int
)
