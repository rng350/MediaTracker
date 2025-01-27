package com.rng350.mediatracker.movies.usecases

import com.rng350.mediatracker.common.database.MovieDao
import com.rng350.mediatracker.movies.MovieActorAndRolesInFilm
import com.rng350.mediatracker.movies.MovieDetails
import com.rng350.mediatracker.movies.MovieGenre
import com.rng350.mediatracker.movies.MovieStaff
import com.rng350.mediatracker.movies.RoleAndImportance
import javax.inject.Inject

class GetMovieDetailsFromDatabaseUseCase @Inject constructor(
    val movieDao: MovieDao
) {
    suspend operator fun invoke(movieId: Int): MovieDetails? {
        val fetchedMovieDetails = movieDao.getMovieDetails(movieId)
        if (fetchedMovieDetails.isEmpty()) return null
        else {
            val firstMovieDetailsRow = fetchedMovieDetails.first()
            val genresMap = fetchedMovieDetails
                .filter { it.genreId!=null }
                .map { MovieGenre(genreId = it.genreId!!, genreName = it.genreName!!) }
                .groupBy { it.genreId }
            val directorsMap = fetchedMovieDetails
                .filter { it.directorPersonId!=null }
                .groupBy { it.directorPersonId!! }
            val directors = directorsMap.keys.mapNotNull { directorId ->
                val first = directorsMap[directorId]?.first()
                if (first!=null)
                    MovieStaff(
                        personId = directorId,
                        personName = first.directorPersonName ?: "",
                        personProfilePicUri = null,
                        personProfilePicUrl = null)
                else null
            }
            val actors = fetchedMovieDetails
                .filter { it.actorPersonId!=null }
                .groupBy { it.actorPersonId!! }
            val actorsAndRoles = actors.keys.map { actorId ->
                val actorRoles = actors[actorId]?.mapNotNull { role ->
                    if (role.actingRoleCharacterName!=null && role.actingRoleCastId!=null && role.actingRoleOrderOfImportance!=null) {
                        RoleAndImportance(
                            characterName = role.actingRoleCharacterName,
                            castingId = role.actingRoleCastId,
                            orderOfImportance = role.actingRoleOrderOfImportance
                        )
                    } else null
                }?.distinctBy { it.castingId } ?: listOf()
                MovieActorAndRolesInFilm(
                    personId = actorId,
                    personName = actors[actorId]?.first()?.actorPersonName ?: "",
                    personRoles = actorRoles,
                    personProfilePicUrl = actors[actorId]?.first()?.actorPersonProfilePicUrl,
                    personProfilePicUri = actors[actorId]?.first()?.actorPersonProfilePicUri,
                    orderOfImportance = actorRoles.minOf { it.orderOfImportance }
                )
            }.sortedBy { it.orderOfImportance }
            return MovieDetails(
                movieId = firstMovieDetailsRow.movieId.toString(),
                movieTitle = firstMovieDetailsRow.movieTitle,
                movieOriginalTitle = firstMovieDetailsRow.movieOriginalTitle,
                movieOverview = firstMovieDetailsRow.moviePremise,
                movieReleaseDate = firstMovieDetailsRow.movieReleaseDate,
                movieGenres = genresMap.keys.mapNotNull { genreId ->
                    val first = genresMap[genreId]?.first()
                    if (first!=null)
                        MovieGenre(first.genreId, first.genreName)
                    else null
                },
                movieDirectors = directors,
                movieActors = actorsAndRoles,
                moviePosterUrl = firstMovieDetailsRow.moviePosterUrl,
                isLiked = firstMovieDetailsRow.isLiked,
                isOnWatchlist = firstMovieDetailsRow.isWatchlisted,
                hasBeenWatched = firstMovieDetailsRow.hasBeenWatched
            )
        }
    }
}