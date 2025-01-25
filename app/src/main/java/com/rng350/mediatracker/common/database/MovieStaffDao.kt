package com.rng350.mediatracker.common.database

import androidx.room.Dao
import androidx.room.Upsert
import com.rng350.mediatracker.movies.MovieActingRole
import com.rng350.mediatracker.movies.MovieDirector
import com.rng350.mediatracker.movies.MovieStaff

@Dao
interface MovieStaffDao {
    @Upsert
    suspend fun upsertMovieStaff(movieStaff: MovieStaff)

    @Upsert
    suspend fun upsertMovieStaff(movieStaff: List<MovieStaff>)

    @Upsert
    suspend fun upsertMovieActor(movieActingRole: MovieActingRole)

    @Upsert
    suspend fun upsertMovieActors(movieActingRoles: List<MovieActingRole>)

    @Upsert
    suspend fun upsertMovieDirectors(movieDirectors: List<MovieDirector>)
}