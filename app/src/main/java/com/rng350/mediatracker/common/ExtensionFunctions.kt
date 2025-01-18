package com.rng350.mediatracker.common

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.toDisplay(): String {
    return this.format(DateTimeFormatter.ofPattern("dd LLLL yyyy"))
}