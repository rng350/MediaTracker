package com.rng350.mediatracker.movies

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "movie_table")
data class Movie(
    @PrimaryKey
    @ColumnInfo(name="movie_id", index = true)
    val movieId: Int,
    @ColumnInfo(name="movie_title", index = true)
    val movieTitle: String,
    @ColumnInfo(name="movie_original_title")
    val movieOriginalTitle: String,
    @ColumnInfo(name="movie_release_date", index = true)
    val movieReleaseDate: LocalDate? = null,
    @ColumnInfo(name="movie_premise")
    val moviePremise: String,
    @ColumnInfo(name="movie_poster_uri")
    val moviePosterUri: Uri? = null,
    @ColumnInfo(name="movie_poster_url")
    val moviePosterUrl: String?
)