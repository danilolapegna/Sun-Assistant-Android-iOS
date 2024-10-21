package com.sunassistant.rest.meteo

import io.ktor.http.URLBuilder
import io.ktor.http.takeFrom

class OpenUVApiConfig {

    companion object {
        private const val BASE_URL = "https://api.openuv.io/api/v1/uv"
        private const val LATITUDE_KEY = "lat"
        private const val LONGITUDE_KEY = "lng"

        fun createUrl(latitude: Double, longitude: Double): String {
            return URLBuilder().apply {
                takeFrom(BASE_URL)
                parameters.append(LATITUDE_KEY, latitude.toString())
                parameters.append(LONGITUDE_KEY, longitude.toString())

            }.buildString()
        }
    }
}
