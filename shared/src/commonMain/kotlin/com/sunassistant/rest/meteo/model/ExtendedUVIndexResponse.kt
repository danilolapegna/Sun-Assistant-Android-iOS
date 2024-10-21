package com.sunassistant.rest.meteo.model

import com.sunassistant.rest.meteo.AbstractUVIndexResponse


class FullUVIndexResponse(
    private val uvIndex: Double?,
    private val USAirQualityIndex: Double?,
    private val EUAirQualityIndex: Double?,
    val isDay: Boolean?,
    val forecastPreviews: List<ForecastItem>?,
    val lastUvIndexFetchedAt: Long?,
    val locationName: String?,
    val currentWeather: Int?,
    val latitude: Double?,
    val longitude: Double,
    val currentTemperature: Double?,
    val todaySunshineDuration: Double?,
    val exception: Exception? = null,
    val originalResponse: AbstractUVIndexResponse
)  {

     fun uvIndexCurrent(): Double? = uvIndex
     fun USAirQualityCurrent(): Double? = USAirQualityIndex
     fun EUAirQualityCurrent(): Double? = EUAirQualityIndex
     fun getUvIndexFetchedTimeInEpochMs(): Long? = lastUvIndexFetchedAt

}
