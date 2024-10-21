package com.sunassistant.rest.meteo

import kotlinx.datetime.TimeZone

class OpenMeteoForecastApiConfig {

    companion object {
        private const val BASE_URL = "https://api.open-meteo.com/v1/forecast"

        fun createUrl(latitude: Double, longitude: Double, params: Map<String, String>): String {
            val queryParams = params.map { "${it.key}=${it.value}" }.joinToString("&")
            val timeZone = TimeZone.currentSystemDefault().id
            return "$BASE_URL?latitude=$latitude&longitude=$longitude&timezone=$timeZone&temperature_unit=celsius&$queryParams"
        }
    }
}