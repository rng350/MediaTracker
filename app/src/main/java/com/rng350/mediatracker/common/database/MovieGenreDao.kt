package com.rng350.mediatracker.common.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.rng350.mediatracker.movies.MovieGenre
import com.rng350.mediatracker.movies.MovieGenreAssociation

@Dao
interface MovieGenreDao {
    @Transaction
    suspend fun upsertGenresAndGetIdsBack(movieGenres: List<MovieGenre>): List<Int> {
        val rowIds = upsertGenres(movieGenres)
        return (getGenreIdFromRowId(rowIds))
    }

    @Upsert
    suspend fun upsertGenres(movieGenre: List<MovieGenre>): List<Long>

    @Upsert
    suspend fun upsertGenreAssociations(movieGenreAssociation: List<MovieGenreAssociation>)

    @Query("SELECT genre_id FROM movie_genre_table WHERE rowid IN (:rowIds)")
    suspend fun getGenreIdFromRowId(rowIds: List<Long>): List<Int>
}