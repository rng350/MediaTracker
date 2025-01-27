package com.rng350.mediatracker.common

import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun LocalDate.toDisplay(): String {
    return this.format(DateTimeFormatter.ofPattern("dd LLLL yyyy"))
}

fun LocalDate.toMonthAndYearDisplay(): String {
    val month = when(this.month) {
        Month.JANUARY -> "January"
        Month.FEBRUARY -> "February"
        Month.MARCH -> "March"
        Month.APRIL -> "April"
        Month.MAY -> "May"
        Month.JUNE -> "June"
        Month.JULY -> "July"
        Month.AUGUST -> "August"
        Month.SEPTEMBER -> "September"
        Month.OCTOBER -> "October"
        Month.NOVEMBER -> "November"
        Month.DECEMBER -> "December"
        null -> "Invalid Date"
    }
    return "$month ${this.year}"
}

fun String.toLocalDate(): LocalDate? {
    var localDate: LocalDate? = null
    try {
        localDate = LocalDate.parse(this, DateTimeFormatter.ISO_LOCAL_DATE)
    } catch (_: DateTimeParseException) {}
    return localDate
}