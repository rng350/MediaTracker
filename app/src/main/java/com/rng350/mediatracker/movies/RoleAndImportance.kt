package com.rng350.mediatracker.movies

import kotlinx.serialization.Serializable

@Serializable
data class RoleAndImportance(
    val characterName: String,
    val castingId: Int,
    val orderOfImportance: Int
)