package com.rng350.mediatracker.featuredmovies

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "featured_movies_trending_this_week_table")
data class FeaturedMovieTrendingThisWeek(
    @ColumnInfo(name = "movie_id")
    @PrimaryKey
    override val movieId: Int,
    @ColumnInfo(name = "movie_title")
    override val movieTitle: String,
    @ColumnInfo(name = "movie_poster_url")
    override val moviePosterUrl: String?,
    @ColumnInfo(name = "order_in_list", index = true)
    override val orderInList: Int,
    @ColumnInfo(name = "date_of_featuring")
    override val dateOfFeaturing: LocalDate
): FeaturedMovie(
    movieId = movieId,
    movieTitle = movieTitle,
    moviePosterUrl = moviePosterUrl,
    orderInList = orderInList,
    dateOfFeaturing = dateOfFeaturing
)