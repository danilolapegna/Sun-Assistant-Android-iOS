package com.sunassistant.rest.meteo

import com.sunassistant.rest.ApiService
import com.sunassistant.rest.meteo.model.OpenMeteoAirQualityResponse
import io.ktor.client.statement.*

class OpenMeteoAirQualityService : ApiService() {
    suspend fun fetchUvIndex(latitude: Double, longitude: Double): OpenMeteoAirQualityResponse {
        val params = mapOf(
            "current" to "uv_index",
            "hourly" to "uv_index",
            "daily" to "uv_index",
            "forecast_days" to "7"
        )
        val urlString = OpenMeteoAirQualityApiConfig.createUrl(latitude, longitude, params)
        val response = apiClient.makeGetRequest(urlString)
        return json.decodeFromString(
            OpenMeteoAirQualityResponse.serializer(),
            response.bodyAsText()
        )
    }

    suspend fun fetchAirQuality(latitude: Double, longitude: Double): OpenMeteoAirQualityResponse {
        val params = mapOf(
            "current" to "european_aqi,us_aqi",
            "hourly" to "european_aqi,us_aqi"
        )
        val urlString = OpenMeteoAirQualityApiConfig.createUrl(latitude, longitude, params)
        val response = apiClient.makeGetRequest(urlString)
        return json.decodeFromString(
            OpenMeteoAirQualityResponse.serializer(),
            response.bodyAsText()
        )
    }
}