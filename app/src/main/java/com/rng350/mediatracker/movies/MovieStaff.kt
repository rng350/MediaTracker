package com.rng350.mediatracker.movies

import android.net.Uri
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
    val personName: String,
    @ColumnInfo(name = "person_profile_picture_uri")
    val personProfilePicUri: Uri? = null,
    @ColumnInfo(name = "person_profile_pic_url")
    val personProfilePicUrl: String?
)
