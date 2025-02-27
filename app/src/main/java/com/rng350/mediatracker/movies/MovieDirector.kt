package com.rng350.mediatracker.movies

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "movie_director_table",
    primaryKeys = ["movie_id", "person_id"],
    foreignKeys = [
        ForeignKey(
            entity = Movie::class,
            parentColumns = ["movie_id"],
            childColumns = ["movie_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MovieStaff::class,
            parentColumns = ["person_id"],
            childColumns = ["person_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["movie_id", "person_id"]),
        Index(value = ["person_id", "movie_id"])
    ]
)
data class MovieDirector(
    @ColumnInfo("movie_id")
    val movieId: Int,
    @ColumnInfo("person_id")
    val personId: Int
)