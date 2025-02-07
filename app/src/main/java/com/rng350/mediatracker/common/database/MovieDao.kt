package com.rng350.mediatracker.common.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.rng350.mediatracker.featuredmovies.FeaturedMovieNowPlaying
import com.rng350.mediatracker.featuredmovies.FeaturedMovieTrendingThisWeek
import com.rng350.mediatracker.featuredmovies.FeaturedMovieTrendingToday
import com.rng350.mediatracker.featuredmovies.FeaturedMovieUpcoming
import com.rng350.mediatracker.movies.LikedMovie
import com.rng350.mediatracker.movies.Movie
import com.rng350.mediatracker.movies.MovieDetailsFetched
import com.rng350.mediatracker.movies.MovieForDisplay
import com.rng350.mediatracker.movies.MovieUserStatus
import com.rng350.mediatracker.movies.WatchedMovie
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovieToWatchedList(watchedMovie: WatchedMovie)

    @Delete
    suspend fun removeMovieFromWatchedlist(watchedMovie: WatchedMovie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovieToLikedList(likedMovie: LikedMovie)

    @Delete
    suspend fun removeMovieFromLikedList(likedMovie: LikedMovie)

    @Query("SELECT * FROM movie_watchlist_table WHERE movie_id=:movieId")
    suspend fun getWatchlistedMovie(movieId: Int): WatchlistedMovie?

    @Query("SELECT * FROM movie_table WHERE movie_id=:movieId")
    suspend fun getMovie(movieId: Int): Movie?

    @Query("""
        SELECT 
            m.movie_id AS movieId,
            m.movie_title AS movieTitle,
            m.movie_original_title AS movieOriginalTitle,
            m.movie_release_date AS movieReleaseDate,
            m.movie_premise AS moviePremise,
            m.movie_poster_uri AS moviePosterUri,
            m.movie_poster_url AS moviePosterUrl, 
            CASE 
                WHEN EXISTS (
                    SELECT 1 
                    FROM liked_movie_table 
                    WHERE liked_movie_table.movie_id = m.movie_id
                ) THEN 1 
                ELSE 0
            END AS isLiked,
            CASE 
                WHEN EXISTS (
                    SELECT 1 
                    FROM movie_watchlist_table 
                    WHERE movie_watchlist_table.movie_id = m.movie_id
                ) THEN 1 
                ELSE 0
            END AS isWatchlisted,
            CASE 
                WHEN EXISTS (
                    SELECT 1 
                    FROM watched_movie_table 
                    WHERE watched_movie_table.movie_id = m.movie_id
                ) THEN 1 
                ELSE 0
            END AS hasBeenWatched, 
            movie_genre_table.genre_id AS genreId, 
            movie_genre_table.genre_name AS genreName, 
            movie_actor_table.cast_id AS actingRoleCastId,
            movie_actor_table.character_name AS actingRoleCharacterName, 
            movie_actor_table.order_of_importance AS actingRoleOrderOfImportance, 
            actor_staff_table.person_id AS actorPersonId, 
            actor_staff_table.person_name AS actorPersonName, 
            actor_staff_table.person_profile_pic_url AS actorPersonProfilePicUrl, 
            actor_staff_table.person_profile_picture_uri AS actorPersonProfilePicUri,
            director_staff_table.person_id AS directorPersonId,
            director_staff_table.person_name AS directorPersonName,
            director_staff_table.person_profile_pic_url AS directorPersonProfilePicUrl,
            director_staff_table.person_profile_picture_uri AS directorPersonProfilePicUri
        FROM movie_table m
        LEFT JOIN movie_actor_table ON m.movie_id=movie_actor_table.movie_id 
        LEFT JOIN movie_staff_table AS actor_staff_table ON movie_actor_table.person_id=actor_staff_table.person_id 
        LEFT JOIN movie_director_table ON m.movie_id=movie_director_table.movie_id 
        LEFT JOIN movie_staff_table AS director_staff_table ON movie_director_table.person_id=director_staff_table.person_id 
        LEFT JOIN movie_genre_association_table ON m.movie_id=movie_genre_association_table.movie_id 
        LEFT JOIN movie_genre_table ON movie_genre_association_table.genre_id=movie_genre_table.genre_id 
        WHERE m.movie_id=:movieId
    """)
    suspend fun getMovieDetails(movieId: Int): List<MovieDetailsFetched>

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
            WHERE movie_id IN (SELECT movie_id FROM movie_watchlist_table)
            ORDER BY 
                CASE 
                    WHEN movie_release_date IS NULL THEN 3 
                    WHEN movie_release_date > strftime('%Y-%m-%d', 'now', 'localtime') THEN 2 
                    ELSE 1 
                END ASC,
                CASE WHEN movie_release_date <= strftime('%Y-%m-%d', 'now', 'localtime') THEN movie_title END ASC,
                CASE WHEN movie_release_date > strftime('%Y-%m-%d', 'now', 'localtime') THEN movie_release_date END ASC, 
                CASE WHEN movie_release_date > strftime('%Y-%m-%d', 'now', 'localtime') THEN movie_title END ASC,
                CASE WHEN movie_release_date IS NULL THEN movie_title END ASC
        """
    )
    fun getAllWatchlistedMovies(): Flow<List<MovieForDisplay>>

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
            WHERE movie_id IN (SELECT movie_id FROM watched_movie_table)
            ORDER BY movie_title ASC
        """
    )
    fun getAllWatchedMovies(): Flow<List<MovieForDisplay>>

    @Query("""
        SELECT 
            CASE WHEN (
                    SELECT movie_id 
                    FROM liked_movie_table 
                    WHERE movie_id=:movieId
            ) IS NOT NULL THEN 1 ELSE 0 END AS isLiked,
            CASE WHEN (
                SELECT movie_id 
                FROM movie_watchlist_table 
                WHERE movie_id=:movieId
            ) IS NOT NULL THEN 1 ELSE 0 END AS isInWatchlist,
            CASE WHEN ( 
                SELECT movie_id 
                FROM watched_movie_table 
                WHERE movie_id=:movieId
            ) IS NOT NULL THEN 1 ELSE 0 END AS hasBeenWatched 
        LIMIT 1
    """)
    suspend fun getMovieUserStatus(movieId: Int): MovieUserStatus

    @Query("SELECT * FROM featured_movies_now_playing_table")
    suspend fun getMoviesNowPlaying(): List<FeaturedMovieNowPlaying>

    @Query("SELECT * FROM featured_movies_trending_today_table")
    suspend fun getMoviesTrendingToday(): List<FeaturedMovieTrendingToday>

    @Query("SELECT * FROM featured_movies_trending_this_week_table")
    suspend fun getMoviesTrendingThisWeek(): List<FeaturedMovieTrendingThisWeek>

    @Query("SELECT * FROM featured_movies_upcoming_table")
    suspend fun getFeaturedMoviesUpcoming(): List<FeaturedMovieUpcoming>

    @Insert
    suspend fun insertMoviesNowPlaying(movies: List<FeaturedMovieNowPlaying>)

    @Insert
    suspend fun insertMoviesTrendingToday(movies: List<FeaturedMovieTrendingToday>)

    @Insert
    suspend fun insertMoviesTrendingThisWeek(movies: List<FeaturedMovieTrendingThisWeek>)

    @Insert
    suspend fun insertMoviesUpcoming(movies: List<FeaturedMovieUpcoming>)

    @Query("DELETE FROM featured_movies_now_playing_table")
    suspend fun deleteAllMoviesNowPlaying(): Int

    @Query("DELETE FROM featured_movies_trending_today_table")
    suspend fun deleteAllMoviesTrendingToday(): Int

    @Query("DELETE FROM featured_movies_trending_this_week_table")
    suspend fun deleteAllMoviesTrendingThisWeek(): Int

    @Query("DELETE FROM featured_movies_upcoming_table")
    suspend fun deleteAllMoviesUpcoming(): Int
}