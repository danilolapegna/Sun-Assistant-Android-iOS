package com.sunassistant.rest.meteo.model

import com.sunassistant.rest.meteo.AbstractUVIndexResponse
import com.sunassistant.util.TimeUtils.isToday
import com.sunassistant.util.toInstantOrNull
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


enum class UVCalculationMode {
    CURRENT_ONLY,
    HOURLY,
    INTERPOLATED
}

/*
 * true: just grab the latest
 * false: do some interpolation with multiple sources
 *
 */
private val uvCalculationMode: UVCalculationMode = UVCalculationMode.INTERPOLATED

@Serializable
class UvIndexDailyForecast(val dailyDate: Instant, val uvIndex: Double?)

@Serializable
class OpenMeteoAirQualityResponse : AbstractUVIndexResponse() {
    val latitude: Double? = null
    val longitude: Double? = null
    val elevation: Double? = null

    @SerialName("generationtime_ms")
    val generationTimeMs: Double? = null

    @SerialName("utc_offset_seconds")
    val utcOffsetSeconds: Int? = null

    val timezone: String? = null

    @SerialName("timezone_abbreviation")
    val timezoneAbbreviation: String? = null

    @SerialName("current")
    val current: CurrentData? = null

    @SerialName("hourly")
    val hourly: HourlyData? = null

    @Serializable
     class CurrentData {
        val time: String? = null
        @SerialName("uv_index") val uvIndex: Double? = null

        @SerialName("european_aqi")
        val europeanAqi: Double? = null

        @SerialName("us_aqi")
        val usAqi: Double? = null
    }

    @Serializable
    class HourlyData {
        val time: List<String>? = null
        @SerialName("uv_index") val uvIndex: List<Double?>? = null

        @SerialName("european_aqi")
        val europeanAqi: List<Double?>? = null

        @SerialName("us_aqi")
        val usAqi: List<Double?>? = null
    }

    override fun getUvIndexFetchedTimeInEpochMs(): Long? {
        return when (uvCalculationMode) {
            UVCalculationMode.CURRENT_ONLY -> current?.time?.toInstantOrNull()?.toEpochMilliseconds()
            UVCalculationMode.HOURLY -> findMostRecentPastTime(hourly?.time)
            UVCalculationMode.INTERPOLATED -> Clock.System.now().toEpochMilliseconds()
        }
    }

    override fun uvIndexCurrent(): Double? {
        return when (uvCalculationMode) {
            UVCalculationMode.CURRENT_ONLY -> current?.uvIndex
            UVCalculationMode.HOURLY -> getHourlyValue(hourly?.uvIndex, hourly?.time)
            UVCalculationMode.INTERPOLATED -> getInterpolatedValue(
                hourly?.uvIndex,
                hourly?.time,
                current?.uvIndex
            )
        }
    }

    fun USAirQualityCurrent(): Double? {
        return when (uvCalculationMode) {
            UVCalculationMode.CURRENT_ONLY -> current?.usAqi
            UVCalculationMode.HOURLY -> getHourlyValue(hourly?.usAqi, hourly?.time)
            UVCalculationMode.INTERPOLATED -> getInterpolatedValue(
                hourly?.usAqi,
                hourly?.time,
                backupValueForFailure = current?.usAqi
            )
        }
    }

    fun EUAirQualityCurrent(): Double? {
        return when (uvCalculationMode) {
            UVCalculationMode.CURRENT_ONLY -> current?.europeanAqi
            UVCalculationMode.HOURLY -> getHourlyValue(hourly?.europeanAqi, hourly?.time)
            UVCalculationMode.INTERPOLATED -> getInterpolatedValue(
                hourly?.europeanAqi,
                hourly?.time,
                backupValueForFailure = current?.europeanAqi
            )
        }
    }

    private fun getHourlyValue(dataList: List<Double?>?, timeList: List<String>?): Double? {
        if (timeList == null) return null
        val index = findClosestPastIndex(timeList, getCurrentTime()) ?: -1
        return dataList?.getOrNull(index)
    }

    private fun getInterpolatedValue(
        dataList: List<Double?>?,
        timeList: List<String>?,
        backupValueForFailure: Double? = null
    ): Double? {
        if (timeList == null) return backupValueForFailure
        val now = getCurrentTime()
        val pastIndex = findClosestPastIndex(timeList, now)
        val futureIndex = findClosestFutureIndex(timeList, now)
        var finalValue: Double? = backupValueForFailure

        if (pastIndex != null && futureIndex != null && dataList != null) {
            val pastTime = timeList.getOrNull(pastIndex)?.toInstantOrNull()
            val futureTime = timeList.getOrNull(futureIndex)?.toInstantOrNull()
            val pastValue = dataList.getOrNull(pastIndex)
            val futureValue = dataList.getOrNull(futureIndex)
            if (pastTime != null && futureTime != null && pastValue != null && futureValue != null) {
                finalValue = interpolate(pastTime, futureTime, pastValue, futureValue, now)
            }
        }
        return finalValue
    }

    private fun getCurrentTime(): Instant = Clock.System.now()

    fun getTodayInstants(timeList: List<String>): List<Instant> {
        return convertToInstantList(timeList).filter { isToday(it) == true }
    }

    private fun convertToInstantList(timeList: List<String>): List<Instant> {
        return timeList.mapNotNull { it.toInstantOrNull() }
    }

    private fun findClosestPastIndex(timeList: List<String>, currentTime: Instant): Int? {
        val instantList = convertToInstantList(timeList)
        return instantList.indexOfLast { it <= currentTime }
            .takeIf { it >= 0 }
    }

    private fun findClosestFutureIndex(timeList: List<String>, currentTime: Instant): Int? {
        val instantList = convertToInstantList(timeList)
        return instantList.indexOfFirst { it > currentTime }
            .takeIf { it >= 0 }
    }

    private fun findMostRecentPastTime(timeList: List<String>?): Long? {
        if (timeList == null) return null
        val now = getCurrentTime()
        val instantList = convertToInstantList(timeList)
        return instantList.filter { it <= now }
            .maxOrNull()
            ?.toEpochMilliseconds()
    }

    private fun interpolate(
        pastTime: Instant,
        futureTime: Instant,
        pastValue: Double,
        futureValue: Double,
        currentTime: Instant,
        isQuadraticInterpolation: Boolean = false
    ): Double {
        return if (isQuadraticInterpolation) {
            interpolateQuadraticInternal(pastTime, futureTime, pastValue, futureValue, currentTime)
        } else {
            interpolateLinearInternal(pastTime, futureTime, pastValue, futureValue, currentTime)
        }
    }

    private fun interpolateLinearInternal(
        pastTime: Instant,
        futureTime: Instant,
        pastValue: Double,
        futureValue: Double,
        currentTime: Instant
    ): Double {
        val durationPastToNow = currentTime.toEpochMilliseconds() - pastTime.toEpochMilliseconds()
        val durationPastToFuture = futureTime.toEpochMilliseconds() - pastTime.toEpochMilliseconds()
        if (durationPastToFuture == 0L) {
            return pastValue
        }
        val ratio =
            durationPastToNow.toDouble() / durationPastToFuture.toDouble()
        return pastValue + ratio * (futureValue - pastValue)
    }

    private fun interpolateQuadraticInternal(
        pastTime: Instant,
        futureTime: Instant,
        pastValue: Double,
        futureValue: Double,
        currentTime: Instant
    ): Double {
        val durationPastToNow = currentTime - pastTime
        val durationPastToFuture = futureTime - pastTime
        if (durationPastToFuture.inWholeMilliseconds == 0L) {
            return pastValue // Avoid division by zero
        }
        val ratio =
            durationPastToNow.inWholeMilliseconds.toDouble() / durationPastToFuture.inWholeMilliseconds
        val midpointValue = (pastValue + futureValue) / 2
        val t = 2 * (ratio - 0.5) // Transform ratio to range -1 to 1
        val quadraticFactor = 1 - t * t // Quadratic equation (flipped parabola)
        return midpointValue + quadraticFactor * (midpointValue - pastValue)
    }
}

fun getDetailedForecastItemsForSameDay(
    forecastsMap: LinkedHashMap<Instant, MutableList<UvIndexDailyForecast>>?,
    targetInstant: Instant?
): MutableList<UvIndexDailyForecast>? {
    forecastsMap ?: return null
    targetInstant ?: return null

    val timeZone =  TimeZone.currentSystemDefault()
    val targetDate = targetInstant.toLocalDateTime(timeZone).date

    for ((keyInstant, forecastsList) in forecastsMap) {
        val keyDate = keyInstant.toLocalDateTime(timeZone).date
        if (keyDate == targetDate) {
            return forecastsList
        }
    }

    return null
}

fun getDetailedUvIndexForecasts(response: OpenMeteoAirQualityResponse?): LinkedHashMap<Instant, MutableList<UvIndexDailyForecast>>? {
    val hourlyData = response?.hourly ?: return null
    val times = hourlyData.time ?: return null
    val uvIndexes = hourlyData.uvIndex ?: return null

    if (times.size != uvIndexes.size) return null

    val timeZone = TimeZone.currentSystemDefault()
    val forecasts = LinkedHashMap<Instant, MutableList<UvIndexDailyForecast>>()

    try {
        times.forEachIndexed { index, timeString ->
            val instant = timeString.toInstantOrNull() ?: return null
            val dailyInstant = LocalDateTime.parse(timeString.substring(0, 10) + "T00:01")
                .toInstant(timeZone)
            val uvIndex = uvIndexes[index]

            val forecastForDay = forecasts.getOrPut(dailyInstant) { mutableListOf() }
            forecastForDay.add(UvIndexDailyForecast(instant, uvIndex))
        }
    } catch (e: Exception) {
        return null
    }

    return forecasts
}