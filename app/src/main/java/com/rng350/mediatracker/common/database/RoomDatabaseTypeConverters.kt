package com.rng350.mediatracker.common.database

import android.net.Uri
import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object RoomDatabaseTypeConverters {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @TypeConverter
    @JvmStatic
    fun toOffsetDateTime(value: String?): OffsetDateTime? {
        return value?.let {
            return formatter.parse(value, OffsetDateTime::from)
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromOffsetDateTime(date: OffsetDateTime?): String? {
        return date?.format(formatter)
    }

    @TypeConverter
    @JvmStatic
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let {
            return LocalDate.parse(it)
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.toString()
    }

    @TypeConverter
    @JvmStatic
    fun toString(value: Uri?): String? {
        value?.let {
            return value.toString()
        }
        return null
    }

    @TypeConverter
    @JvmStatic
    fun toURI(value: String?): Uri? {
        value?.let {
            return Uri.parse(value)
        }
        return null
    }
}