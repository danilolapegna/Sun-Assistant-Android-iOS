package com.sunassistant.rest.meteo

import GeocodingResponse
import com.sunassistant.rest.ApiService
import io.ktor.client.statement.bodyAsText

class GeocodingService : ApiService() {

    suspend fun fetchReverseGeocodeName(latitude: Double, longitude: Double): GeocodingResponse {
        val urlString = GeocodingApiConfig.createUrl(latitude, longitude)
        val response = apiClient.makeGetRequest(urlString)
        val text = response.bodyAsText()
        return json.decodeFromString(GeocodingResponse.serializer(), text)
    }
}
