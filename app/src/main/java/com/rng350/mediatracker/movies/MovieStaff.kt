package com.rng350.mediatracker.movies

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "movie_staff_table"
)
data class MovieStaff(
    @PrimaryKey
    @ColumnInfo(name = "person_id", index = true)
    val personId: Int,
    @ColumnInfo(name = "person_name", index = true)
    val personName: String
)
