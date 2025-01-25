package com.rng350.mediatracker.movies

import android.net.Uri

data class MovieCharacter(
    val personId: Int,
    val personName: String,
    val personRoles: List<String>,
    val personProfilePicUrl: String?,
    val personProfilePicUri: Uri? = null,
    val orderOfImportance: Int
)
