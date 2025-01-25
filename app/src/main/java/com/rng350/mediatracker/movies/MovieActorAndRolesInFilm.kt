package com.rng350.mediatracker.movies

import android.net.Uri

data class MovieActorAndRolesInFilm(
    val personId: Int,
    val personName: String,
    val personRoles: List<RoleAndImportance>,
    val personProfilePicUrl: String?,
    val personProfilePicUri: Uri? = null,
    val orderOfImportance: Int
)
