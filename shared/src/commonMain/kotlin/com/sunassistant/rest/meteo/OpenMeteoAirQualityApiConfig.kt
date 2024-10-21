package com.sunassistant.rest.meteo

class OpenMeteoAirQualityApiConfig {

    companion object {
        private const val BASE_URL = "https://air-quality-api.open-meteo.com/v1/air-quality"

        fun createUrl(latitude: Double, longitude: Double, params: Map<String, String>): String {
            val queryParams = params.map { "${it.key}=${it.value}" }.joinToString("&")
            return "$BASE_URL?latitude=$latitude&longitude=$longitude&$queryParams"
        }
    }
}