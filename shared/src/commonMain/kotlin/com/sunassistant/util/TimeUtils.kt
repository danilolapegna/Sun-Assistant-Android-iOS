package com.sunassistant.util

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.math.abs

object TimeUtils {

    fun isToday(instant: Instant?): Boolean? {
        return instant?.let {
            areSameDay(instant, Clock.System.now())
        }
    }

    fun areSameDay(instant1: Instant, instant2: Instant): Boolean {
        val timeZone = TimeZone.currentSystemDefault()
        val date1 = instant1.toLocalDateTime(timeZone).date
        val date2 = instant2.toLocalDateTime(timeZone).date
        return date1 == date2
    }

    //Default max 480 minutes = 8h
    fun formatTimeInMinutes(
        timeInMinutes: Double,
        maxTimeInMinutes: Double? = 480.0,
        displayZeroUnits: Boolean = false,
        displaySeconds: Boolean = false
    ): String {
        if (maxTimeInMinutes != null && timeInMinutes > maxTimeInMinutes) {
            return if (maxTimeInMinutes >= 60) {
                "+${(maxTimeInMinutes / 60).toInt()}h"
            } else {
                "+${maxTimeInMinutes.toInt()}m"
            }
        }

        val hours = (timeInMinutes / 60).toInt()
        val minutes = (timeInMinutes % 60).toInt()
        val seconds = ((timeInMinutes % 1) * 60).toInt()

        return buildString {
            if (hours > 0 || displayZeroUnits) append("${hours}h ")
            if (minutes > 0 || displayZeroUnits || hours > 0) append("${minutes}m ")
            if (displaySeconds && (seconds > 0 || displayZeroUnits || hours > 0 || minutes > 0)) append(
                "${seconds}s"
            )
        }.trim()
    }

    private fun formatEpochMillis(epochMillis: Long?): String {
        if (epochMillis == null) return "Unknown"
        val instant = Instant.fromEpochMilliseconds(epochMillis)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        return buildString {
            append(localDateTime.date.dayOfMonth.toString().padStart(2, '0'))
            append('/')
            append(localDateTime.date.monthNumber.toString().padStart(2, '0'))
            append('/')
            append(localDateTime.date.year.toString().substring(2))
            append(", ")
            append(localDateTime.hour.toString().padStart(2, '0'))
            append(':')
            append(localDateTime.minute.toString().padStart(2, '0'))
        }
    }

    fun formatSecondsDuration(secondsInput: Double?): String {
        if (secondsInput == 0.0) return "No more for today :)"
        if (secondsInput == null || secondsInput < 0) return "Unknown"

        val totalSeconds = secondsInput.toInt()
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return buildString {
            if (hours > 0) append("${hours}h ")
            if (minutes > 0) append("${minutes}m ")
            if (seconds > 0) append("${seconds}s")
        }.trimEnd()
    }

    fun formatFetchedAtTime(epochMillis: Long?): String {
        return formatEpochMillis(epochMillis)
    }

    fun getClosestPastInstantInList(instants: List<String>): IndexedValue<Instant>? {
        val currentTime = Clock.System.now()
        return instants
            .mapNotNull { it.toInstantOrNull() }
            .filter { it <= currentTime }
            .withIndex()
            .minByOrNull { abs(it.value.epochSeconds - currentTime.epochSeconds) }
    }
}

fun Instant.formatBasic(): String {
    val timeZone: TimeZone = TimeZone.currentSystemDefault()
    val dateTime = this.toLocalDateTime(timeZone)
    return "${
        dateTime.dayOfWeek.name.lowercase().capitalize().substring(0, 3)
    } ${dateTime.dayOfMonth} ${
        dateTime.month.name.lowercase().capitalize().substring(0, 3)
    } ${dateTime.year}"
}

fun Instant.formatBasicTimeOnly(): String {
    val timeZone: TimeZone = TimeZone.currentSystemDefault()
    val dateTime = this.toLocalDateTime(timeZone)
    val minutes = if (dateTime.minute < 10) "0${dateTime.minute}" else "${dateTime.minute}"
    return "${dateTime.hour}:$minutes"
}

fun String.toInstantOrNull(): Instant? = try {
    val timeZone: TimeZone = TimeZone.currentSystemDefault()
    LocalDateTime.parse(this).toInstant(timeZone)
} catch (e: Exception) {
    try {
        val timeZone: TimeZone = TimeZone.currentSystemDefault()
        val withDefaultTime = "${this}T00:01"
        LocalDateTime.parse(withDefaultTime).toInstant(timeZone)
    } catch (e: Exception) {
        null
    }
}