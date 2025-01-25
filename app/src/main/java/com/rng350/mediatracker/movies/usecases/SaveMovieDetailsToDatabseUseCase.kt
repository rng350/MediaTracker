package com.rng350.mediatracker.movies.usecases

import android.net.Uri
import com.rng350.mediatracker.common.Constants.TMDB_IMAGE_SUBFOLDER
import com.rng350.mediatracker.common.SaveImageFromURLUseCase
import com.rng350.mediatracker.common.database.MovieDao
import com.rng350.mediatracker.common.database.MovieGenreDao
import com.rng350.mediatracker.common.database.MovieStaffDao
import com.rng350.mediatracker.movies.Movie
import com.rng350.mediatracker.movies.MovieActingRole
import com.rng350.mediatracker.movies.MovieDetails
import com.rng350.mediatracker.movies.MovieDirector
import com.rng350.mediatracker.movies.MovieGenreAssociation
import com.rng350.mediatracker.movies.MovieStaff
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import javax.inject.Inject

class SaveMovieDetailsToDatabseUseCase @Inject constructor(
    private val saveImageFromUrl: SaveImageFromURLUseCase,
    private val movieDao: MovieDao,
    private val movieStaffDao: MovieStaffDao,
    private val movieGenreDao: MovieGenreDao
) {
    suspend operator fun invoke(movieDetails: MovieDetails) {
        val upsertMovieOperation = GlobalScope.async {
            var posterUri: Uri? = null
            movieDetails.moviePosterUrl?.let { posterUrl ->
                posterUri = saveImageFromUrl(posterUrl, TMDB_IMAGE_SUBFOLDER)
            }
            movieDao.upsertMovie(
                Movie(
                    movieId = movieDetails.movieId.toInt(),
                    movieTitle = movieDetails.movieTitle,
                    movieOriginalTitle = movieDetails.movieOriginalTitle,
                    movieReleaseDate = movieDetails.movieReleaseDate,
                    moviePremise = movieDetails.movieOverview,
                    moviePosterUri = posterUri,
                    moviePosterUrl = movieDetails.moviePosterUrl
                )
            )
        }
        val convertActorsToMovieStaffOperation = movieDetails.movieActors.map { actor ->
            GlobalScope.async {
                var actorProfilePicUri: Uri? = null
                actor.personProfilePicUrl?.let {
                    actorProfilePicUri = saveImageFromUrl(actor.personProfilePicUrl, TMDB_IMAGE_SUBFOLDER)
                }
                MovieStaff(
                    personId = actor.personId,
                    personName = actor.personName,
                    personProfilePicUri = actorProfilePicUri,
                    personProfilePicUrl = actor.personProfilePicUrl
                )
            }
        }
        val upsertActorsAsStaffOperation = GlobalScope.async {
            val actorsAsStaff = convertActorsToMovieStaffOperation.awaitAll()
            movieStaffDao.upsertMovieStaff(actorsAsStaff)
        }
        val upsertDirectorsAsStaffOperation = GlobalScope.async {
            val movieDirectorsAsStaff = movieDetails.movieDirectors.map { director ->
                MovieStaff(
                    personId = director.personId,
                    personName = director.personName,
                    personProfilePicUri = null,
                    personProfilePicUrl = null
                )
            }
            movieStaffDao.upsertMovieStaff(movieDirectorsAsStaff)
        }
        val upsertGenresOperation = GlobalScope.async {
            movieGenreDao.upsertGenresAndGetIdsBack(movieDetails.movieGenres)
        }

        val upsertedMovieId = upsertMovieOperation.await()

        val upsertActingRoleOperation = GlobalScope.async {
            upsertActorsAsStaffOperation.await()
            movieDetails.movieActors.map { actor ->
                val actingRoles = actor.personRoles.map { actingRole ->
                    MovieActingRole(
                        movieId = upsertedMovieId,
                        personId = actor.personId,
                        castingId = actingRole.castingId,
                        characterName = actingRole.characterName,
                        orderOfImportance = actingRole.orderOfImportance
                    )
                }
                movieStaffDao.upsertMovieActors(actingRoles)
            }
        }

        val upsertMovieDirectorsOperation = GlobalScope.async {
            upsertDirectorsAsStaffOperation.await()
            val directors = movieDetails.movieDirectors.map { director ->
                MovieDirector(
                    movieId = upsertedMovieId,
                    personId = director.personId
                )
            }
            movieStaffDao.upsertMovieDirectors(directors)
        }
        val upsertMovieGenreAssociationOperation = GlobalScope.async {
            upsertGenresOperation.await()
            val genreAssociations = movieDetails.movieGenres.map { genre ->
                MovieGenreAssociation(
                    movieId=movieDetails.movieId.toInt(),
                    genreId = genre.genreId
                )
            }
            movieGenreDao.upsertGenreAssociations(genreAssociations)
        }

        upsertActingRoleOperation.await()
        upsertMovieDirectorsOperation.await()
        upsertMovieGenreAssociationOperation.await()
    }
}