package com.sunassistant.rest.meteo.model

import com.sunassistant.util.TimeUtils
import com.sunassistant.util.TimeUtils.isToday
import com.sunassistant.util.UVAssistant
import com.sunassistant.util.formatBasic
import com.sunassistant.util.formatBasicTimeOnly
import com.sunassistant.util.toInstantOrNull
import kotlinx.datetime.Clock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenMeteoForecastResponse(
    val latitude: Double,
    val longitude: Double,
    val elevation: Double,
    @SerialName("generationtime_ms") val generationTimeMs: Double,
    @SerialName("utc_offset_seconds") val utcOffsetSeconds: Int,
    val timezone: String,
    @SerialName("timezone_abbreviation") val timezoneAbbreviation: String,
    @SerialName("minutely_15") val fifteenMinutely: FifteenMinutelyData,
    @SerialName("daily") val daily: DailyData
) {

    val isDay: Boolean? = buildIsDayValue(this)

    var forecastPreviews: List<ForecastItem>? = buildForecastPreviewItems(this)

    val currentWeather: Int? = TimeUtils.getClosestPastInstantInList(fifteenMinutely.time)
        ?.index
        ?.let { index ->
            fifteenMinutely.weatherCode.getOrNull(index)
        }

    val currentTemperature: Double? = TimeUtils.getClosestPastInstantInList(fifteenMinutely.time)
        ?.index
        ?.let { index ->
            fifteenMinutely.temperature.getOrNull(index)
        }

    val todaySunshineDuration: Double? = daily.sunshineDuration.getOrNull(0)

    @Serializable
    data class FifteenMinutelyData(
        val time: List<String>,
        @SerialName("is_day") val isDay: List<Int>,
        @SerialName("weather_code") val weatherCode: List<Int>,
        @SerialName("temperature_2m") val temperature: List<Double>
    )

    @Serializable
    data class DailyData(
        val time: List<String>,
        @SerialName("uv_index_max") val uvIndexMax: List<Double>? = null,
        @SerialName("uv_index_clear_sky_max") val uvIndexClearSkyMax: List<Double>? = null,
        @SerialName("weather_code") val weatherCode: List<Int>? = null,
        @SerialName("sunshine_duration") val sunshineDuration: List<Double>
    ) {
        @SerialName("sunset")
        val sunset: List<String>? = null

        @SerialName("sunrise")
        val sunrise: List<String>? = null

        @SerialName("temperature_2m_max")
        val temperatureMax: List<String>? = null

        @SerialName("temperature_2m_min")
        val temperatureMin: List<String>? = null
    }
}

@Serializable
data class ForecastItem(
    val date: String,
    val uvIndexMax: Double?,
    val uvIndexClearSkyMax: Double?,
    val weatherCode: Int?,
    val sunrise: String?,
    val sunset: String?,
    val temperatureMax: String?,
    val temperatureMin: String?,
    val isToday: Boolean?,
    val todayDetailedForecastItems: MutableList<UvIndexDailyForecast>?
) {
    fun getShortForecastMessage(): String {
        return if (todayDetailedForecastItems.isNullOrEmpty()) {
            UVAssistant.generateShortUVIndexMessage(uvIndexMax, weatherCode)
        } else {
            UVAssistant.generateDetailedUvIndexForecastAdvice(
                todayDetailedForecastItems,
                sunrise,
                sunset,
                weatherCode
            )
        }
    }

    fun getPeakTimeString(): String {
        if (todayDetailedForecastItems.isNullOrEmpty() || uvIndexMax == null) return "Unknown"
        val dayOnlyItems = UVAssistant.getDayOnlyTimes(
            todayDetailedForecastItems,
            sunrise?.toInstantOrNull(),
            sunset?.toInstantOrNull()
        )
        if (dayOnlyItems.isEmpty()) return "Unknown"
        val peakUvForecast = dayOnlyItems.maxByOrNull { it.uvIndex ?: -1.0 }
        val peakUvTime = peakUvForecast?.dailyDate?.formatBasicTimeOnly()
        return peakUvTime ?: "Unknown"
    }

    fun getTemperatureString(): String {
        return UVAssistant.getMinMaxTemperatureString(
            temperatureMax,
            temperatureMin
        ) + "."
    }
}

fun buildIsDayValue(forecastResponse: OpenMeteoForecastResponse): Boolean? {
    val now = Clock.System.now()
    val sunsetTime = forecastResponse.daily.sunset?.getOrNull(0) ?: return null
    val sunsetTimeInstant = sunsetTime.toInstantOrNull() ?: return null
    val sunriseTime = forecastResponse.daily.sunrise?.getOrNull(0) ?: return null
    val sunriseTimeInstant = sunriseTime.toInstantOrNull() ?: return null
    return sunsetTimeInstant > now || now < sunriseTimeInstant
}

fun buildForecastPreviewItems(
    forecastResponse: OpenMeteoForecastResponse,
    todayAirQualityResponse: OpenMeteoAirQualityResponse? = null
): List<ForecastItem>? {
    if (forecastResponse.daily.uvIndexMax.isNullOrEmpty() || forecastResponse.daily.uvIndexClearSkyMax.isNullOrEmpty()) {
        return null
    } else {
        val detailedForecastItems = getDetailedUvIndexForecasts(todayAirQualityResponse)
        val forecastList = mutableListOf<ForecastItem>()
        var i = 0
        forecastResponse.daily.time.forEach { date ->
            val dateInstant = date.toInstantOrNull()
            val isToday = isToday(dateInstant)
            val finalDateString =
                if (isToday == true) "Today" else dateInstant?.formatBasic() ?: date
            val weatherCodeForDay = forecastResponse.daily.weatherCode?.getOrNull(i)
            val sunsetForDay = forecastResponse.daily.sunset?.getOrNull(i)
            val sunriseForDay = forecastResponse.daily.sunrise?.getOrNull(i)
            val temperatureMaxForDay = forecastResponse.daily.temperatureMax?.getOrNull(i)
            val temperatureMinForDay = forecastResponse.daily.temperatureMin?.getOrNull(i)

            val todayDetailedForecastItems = getDetailedForecastItemsForSameDay(
                detailedForecastItems,
                dateInstant
            )
            var uvIndexForDay: Double? = todayDetailedForecastItems
                ?.mapNotNull { it.uvIndex }
                ?.maxOrNull()
            if (uvIndexForDay != null && uvIndexForDay <= 0) uvIndexForDay = null

            forecastList.add(
                ForecastItem(
                    date = finalDateString,
                    uvIndexMax = uvIndexForDay,
                    uvIndexClearSkyMax = null,
                    weatherCode = weatherCodeForDay,
                    sunrise = sunriseForDay,
                    sunset = sunsetForDay,
                    temperatureMax = temperatureMaxForDay,
                    temperatureMin = temperatureMinForDay,
                    isToday = isToday,
                    todayDetailedForecastItems = todayDetailedForecastItems
                )
            )

            i++
        }
        return forecastList
    }
}
