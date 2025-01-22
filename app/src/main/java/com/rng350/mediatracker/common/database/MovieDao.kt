package com.rng350.mediatracker.common.database

import androidx.room.Dao
import androidx.room.Upsert
import com.rng350.mediatracker.movies.Movie

@Dao
interface MovieDao {
    @Upsert
    suspend fun upsert(movie: Movie)

    @Upsert
    suspend fun upsert(movies: List<Movie>)
}