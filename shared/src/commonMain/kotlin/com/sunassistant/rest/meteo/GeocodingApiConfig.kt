package com.sunassistant.rest.meteo


class GeocodingApiConfig {

    companion object {
        private const val GEOCODING_API_KEY = "{Insert here your geocoding api key}"
        private const val BASE_URL = "https://api.bigdatacloud.net/data/reverse-geocode"

        fun createUrl(latitude: Double, longitude: Double, params: Map<String, String>? = null): String {
            val queryParams = params?.map { "${it.key}=${it.value}" }?.joinToString("&") ?: ""
            return "$BASE_URL?latitude=$latitude&longitude=$longitude&key=$GEOCODING_API_KEY&$queryParams"
        }
    }
}