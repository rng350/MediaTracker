package com.rng350.mediatracker.movies

import android.net.Uri
import com.rng350.mediatracker.common.serializers.UriSerializer
import kotlinx.serialization.Serializable

@Serializable
data class MovieActorAndRolesInFilm(
    val personId: Int,
    val personName: String,
    val personRoles: List<RoleAndImportance>,
    val personProfilePicUrl: String?,
    @Serializable(with = UriSerializer::class)
    val personProfilePicUri: Uri? = null,
    val orderOfImportance: Int
)
