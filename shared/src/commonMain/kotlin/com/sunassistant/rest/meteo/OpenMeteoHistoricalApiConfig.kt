package com.sunassistant.rest.meteo

class OpenMeteoHistoricalApiConfig {
    companion object {
        private const val BASE_URL = "https://archive-api.open-meteo.com/v1/archive"

        fun createUrl(latitude: Double, longitude: Double, startDate: String, endDate: String, params: Map<String, String>): String {
            val queryParams = params.entries.joinToString("&") { "${it.key}=${it.value}" }
            return "$BASE_URL?latitude=$latitude&longitude=$longitude&start_date=$startDate&end_date=$endDate&$queryParams"
        }
    }
}
