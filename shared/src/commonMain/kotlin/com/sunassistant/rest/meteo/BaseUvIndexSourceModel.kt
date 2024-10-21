package com.sunassistant.rest.meteo;

import GeocodingResponse
import com.sunassistant.rest.ApiListener
import com.sunassistant.rest.BaseViewModel
import com.sunassistant.rest.meteo.model.FullUVIndexResponse
import com.sunassistant.rest.meteo.model.OpenMeteoAirQualityResponse
import com.sunassistant.rest.meteo.model.OpenMeteoForecastResponse
import com.sunassistant.rest.meteo.model.buildForecastPreviewItems
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlin.math.abs

interface UvIndexListener : ApiListener<FullUVIndexResponse>

class BaseUvIndexSourceModel : BaseViewModel<FullUVIndexResponse>() {
    private val sources: Array<suspend (Double, Double) -> AbstractUVIndexResponse> = arrayOf(
        { lat, lon -> OpenMeteoAirQualityService().fetchUvIndex(lat, lon) },
        { lat, lon -> OpenUVService().fetchUvIndex(lat, lon) }
        /* More sources can be added here */
    )

    // Cache Properties
    private var cache: FullUVIndexResponse? = null
    private var lastCacheUpdateTime: Long = 0
    private var lastLocation: Pair<Double, Double>? = null

    fun fetchUvIndex(latitude: Double, longitude: Double, ignoreCache: Boolean = true) {
        listener?.onLoadStart()
        CoroutineScope(Dispatchers.Main).launch {
            if (handleCache(latitude, longitude, ignoreCache)) return@launch

            val geocodingResponse = fetchReverseGeocodingName(latitude, longitude)
            val geocodingName = geocodingResponse?.getGeocodingName()
            val weatherData = fetchWeatherForecast(latitude, longitude)
            val airQualityData = fetchAirQuality(latitude, longitude)
            val uvIndexResponse =
                processUvSources(latitude, longitude, weatherData, geocodingName, airQualityData)
            uvIndexResponse.let {
                updateCache(it, latitude, longitude)
                listener?.onDataLoaded(it)
                listener?.onLoadEnd()
            } ?: handleFailure()
        }
    }

    private fun handleCache(latitude: Double, longitude: Double, ignoreCache: Boolean): Boolean {
        if (!ignoreCache && shouldFetchFromCache(latitude, longitude)) {
            cache?.let {
                listener?.onDataLoaded(it)
                listener?.onLoadEnd()
                return true
            }
        }
        return false
    }

    private suspend fun fetchWeatherForecast(
        latitude: Double,
        longitude: Double
    ): OpenMeteoForecastResponse? {
        return try {
            OpenMeteoForecastService().fetchWeatherForecast(latitude, longitude)
        } catch (e: Exception) {
            println("Error fetching weather data: $e")
            null
        }
    }

    private suspend fun fetchAirQuality(
        latitude: Double,
        longitude: Double
    ): OpenMeteoAirQualityResponse? {
        return try {
            OpenMeteoAirQualityService().fetchAirQuality(latitude, longitude)
        } catch (e: Exception) {
            println("Error fetching air quality data: $e")
            null
        }
    }

    private suspend fun processUvSources(
        latitude: Double, longitude: Double,
        weatherData: OpenMeteoForecastResponse?,
        geocodingName: String?,
        airQualityData: OpenMeteoAirQualityResponse?
    ): FullUVIndexResponse {
        var lastException: Exception? = null
        for ((index, source) in sources.withIndex()) {
            try {
                val uvIndexResponse = withContext(Dispatchers.IO) { source(latitude, longitude) }
                weatherData?.let {
                    if (uvIndexResponse is OpenMeteoAirQualityResponse) {
                        weatherData.forecastPreviews =
                            buildForecastPreviewItems(weatherData, uvIndexResponse)
                    } else {
                        weatherData.forecastPreviews =
                            buildForecastPreviewItems(weatherData)
                    }
                }
                uvIndexResponse.uvIndexCurrent()?.let { uvIndex ->
                    return createUvIndexResponse(
                        uvIndexResponse,
                        uvIndex,
                        weatherData,
                        geocodingName,
                        latitude,
                        longitude,
                        null,
                        airQualityData
                    )
                }
            } catch (e: Exception) {
                lastException = e
                if (index == sources.lastIndex) break
            }
        }
        return createUvIndexResponse(
            ConcreteUVIndexResponse(),
            null,
            weatherData,
            geocodingName,
            latitude,
            longitude,
            lastException,
            airQualityData
        )
    }

    private fun createUvIndexResponse(
        uvIndexResponse: AbstractUVIndexResponse,
        uvIndex: Double?,
        weatherData: OpenMeteoForecastResponse?,
        geocodingName: String?,
        latitude: Double,
        longitude: Double,
        exception: Exception?,
        airQualityData: OpenMeteoAirQualityResponse?
    ): FullUVIndexResponse {
        return FullUVIndexResponse(
            uvIndex = uvIndex,
            USAirQualityIndex = airQualityData?.USAirQualityCurrent(),
            EUAirQualityIndex = airQualityData?.EUAirQualityCurrent(),
            isDay = weatherData?.isDay,
            forecastPreviews = weatherData?.forecastPreviews,
            lastUvIndexFetchedAt = uvIndexResponse.getUvIndexFetchedTimeInEpochMs(),
            locationName = geocodingName,
            currentWeather = weatherData?.currentWeather,
            currentTemperature = weatherData?.currentTemperature,
            latitude = latitude,
            longitude = longitude,
            todaySunshineDuration = weatherData?.todaySunshineDuration,
            exception = exception,
            originalResponse = uvIndexResponse
        )
    }

    private fun handleFailure() {
        listener?.onException(Exception("Failed to fetch UV index from all sources."))
    }

    private fun shouldFetchFromCache(latitude: Double, longitude: Double): Boolean {
        val timeDiff = Clock.System.now().toEpochMilliseconds() - lastCacheUpdateTime
        if (timeDiff > 5 * 60 * 1000) return false

        val lastLocation = lastLocation ?: return false
        return !isLocationChangedSignificantly(lastLocation, Pair(latitude, longitude))
    }

    private fun isLocationChangedSignificantly(
        oldLocation: Pair<Double, Double>,
        newLocation: Pair<Double, Double>
    ): Boolean {
        val latDiff = abs(oldLocation.first - newLocation.first)
        val lonDiff = abs(oldLocation.second - newLocation.second)
        return latDiff > metersToLatLonDifference(METERS_DIFFERENCE) || lonDiff > metersToLatLonDifference(
            METERS_DIFFERENCE
        )
    }

    private fun updateCache(response: FullUVIndexResponse, latitude: Double, longitude: Double) {
        cache = response
        lastCacheUpdateTime = Clock.System.now().toEpochMilliseconds()
        lastLocation = Pair(latitude, longitude)
    }

    private suspend fun fetchReverseGeocodingName(
        latitude: Double,
        longitude: Double
    ): GeocodingResponse? {
        return try {
            GeocodingService().fetchReverseGeocodeName(latitude, longitude)
        } catch (e: Exception) {
            GeocodingResponse(latitude, longitude)
        }
    }

    companion object {
        private const val METERS_DIFFERENCE = 300.0

        private const val EARTH_RADIUS_METERS = 6_371_000.0

        fun metersToLatLonDifference(meters: Double): Double {
            return meters / EARTH_RADIUS_METERS
        }
    }
}
