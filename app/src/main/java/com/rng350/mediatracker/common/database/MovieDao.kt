package com.rng350.mediatracker.common.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.rng350.mediatracker.movies.Movie
import com.rng350.mediatracker.movies.WatchlistedMovie

@Dao
interface MovieDao {
    @Transaction
    suspend fun upsertMovie(movie: Movie): Int {
        val rowId = upsert(movie)
        return getMovieIdFromRowId(rowId)
    }

    @Upsert
    suspend fun upsert(movie: Movie): Long

    @Query("SELECT movie_id FROM movie_table WHERE rowid=:rowId")
    suspend fun getMovieIdFromRowId(rowId: Long): Int

    @Upsert
    suspend fun upsert(movies: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovieToWatchlist(watchlistedMovie: WatchlistedMovie)

    @Delete
    suspend fun removeMovieFromWatchlist(watchlistedMovie: WatchlistedMovie)

    @Query("SELECT * FROM movie_watchlist_table WHERE movie_id=:movieId")
    suspend fun getWatchlistedMovie(movieId: Int): WatchlistedMovie?

    @Query("SELECT * FROM movie_table WHERE movie_id=:movieId")
    suspend fun getMovie(movieId: Int): Movie?
}