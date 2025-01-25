package com.rng350.mediatracker.common.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Upsert
import com.rng350.mediatracker.movies.MovieActor
import com.rng350.mediatracker.movies.MovieDirector
import com.rng350.mediatracker.movies.MovieStaff

@Dao
interface MovieStaffDao {
    @Upsert
    suspend fun upsertMovieStaff(movieStaff: MovieStaff)

    @Insert
    suspend fun insertMovieActor(movieActor: MovieActor)

    @Insert
    suspend fun insertMovieDirector(movieDirector: MovieDirector)
}