package com.sunassistant.rest.meteo.model

import com.sunassistant.rest.meteo.AbstractUVIndexResponse
import kotlinx.datetime.Clock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenUVIndexResponse(
    val result: Result?
) : AbstractUVIndexResponse() {

    @Serializable
    data class Result(
        @SerialName("uv") val uv: Double,
        @SerialName("uv_time") val uvTime: String,
        @SerialName("uv_max") val uvMax: Double,
        @SerialName("uv_max_time") val uvMaxTime: String,
        @SerialName("ozone") val ozone: Double,
        @SerialName("ozone_time") val ozoneTime: String,
        @SerialName("safe_exposure_time") val safeExposureTime: SafeExposureTime,
        @SerialName("sun_info") val sunInfo: SunInfo
    )

    @Serializable
    data class SafeExposureTime(
        val st1: Int?,
        val st2: Int?,
        val st3: Int?,
        val st4: Int?,
        val st5: Int?,
        val st6: Int?
    )

    @Serializable
    data class SunInfo(
        @SerialName("sun_times") val sunTimes: SunTimes,
        @SerialName("sun_position") val sunPosition: SunPosition
    )

    @Serializable
    data class SunTimes(
        val solarNoon: String,
        val nadir: String,
        val sunrise: String,
        val sunset: String,
        val sunriseEnd: String,
        val sunsetStart: String,
        val dawn: String,
        val dusk: String,
        val nauticalDawn: String,
        val nauticalDusk: String,
        val nightEnd: String,
        val night: String,
        val goldenHourEnd: String,
        val goldenHour: String
    )

    @Serializable
    data class SunPosition(
        val azimuth: Double,
        val altitude: Double
    )

    override fun uvIndexCurrent(): Double? {
        return result?.uv
    }

    /* Approximation: we don't have actual data in this response */
    override fun getUvIndexFetchedTimeInEpochMs(): Long = Clock.System.now().toEpochMilliseconds()
}
