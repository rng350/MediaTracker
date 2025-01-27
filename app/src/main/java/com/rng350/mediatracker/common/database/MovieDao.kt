package com.rng350.mediatracker.common.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.rng350.mediatracker.movies.Movie
import com.rng350.mediatracker.movies.MovieForDisplay
import com.rng350.mediatracker.movies.WatchlistedMovie
import kotlinx.coroutines.flow.Flow

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

    @Query(
        """
            SELECT
                movie_id AS movieId, 
                movie_title AS movieTitle, 
                movie_original_title AS movieOriginalTitle, 
                movie_release_date AS movieReleaseDate, 
                movie_premise AS moviePremise, 
                movie_poster_url AS moviePosterUrl, 
                movie_poster_uri AS moviePosterUri 
            FROM movie_table 
            ORDER BY 
                CASE WHEN movie_release_date IS NULL THEN 1 ELSE 0 END ASC, 
                datetime(movie_release_date) ASC 
        """
    )
    fun getAllWatchlistedMovies(): Flow<List<MovieForDisplay>>
}