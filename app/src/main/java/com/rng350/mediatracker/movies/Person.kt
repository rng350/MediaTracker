package com.rng350.mediatracker.movies

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.OffsetDateTime

@Entity(
    tableName = "person_table"
)
data class Person(
    @PrimaryKey
    @ColumnInfo(name = "person_id", index = true)
    val personId: Int,
    @ColumnInfo(name = "person_name", index = true)
    val personName: String,
    @ColumnInfo(name="last_refreshed_datetime")
    val lastRefreshedDateTime: OffsetDateTime
)
