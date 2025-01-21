package com.rng350.mediatracker.common

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun LocalDate.toDisplay(): String {
    return this.format(DateTimeFormatter.ofPattern("dd LLLL yyyy"))
}

fun String.toLocalDate(): LocalDate? {
    var localDate: LocalDate? = null
    try {
        localDate = LocalDate.parse(this, DateTimeFormatter.ISO_LOCAL_DATE)
    } catch (_: DateTimeParseException) {}
    return localDate
}