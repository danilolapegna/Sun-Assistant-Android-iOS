package com.sunassistant.rest.meteo

import com.sunassistant.coderesources.CommonStrings_System
import com.sunassistant.rest.ApiService
import com.sunassistant.rest.meteo.model.OpenUVIndexResponse
import io.ktor.client.statement.*

class OpenUVService : ApiService() {

    init {
        apiClient.permanentHeaders = mapOf(Pair("x-access-token", CommonStrings_System.open_uv_key))
    }

    suspend fun fetchUvIndex(latitude: Double, longitude: Double): OpenUVIndexResponse {
        val urlString = OpenUVApiConfig.createUrl(latitude, longitude)
        val response = apiClient.makeGetRequest(urlString)
        return json.decodeFromString(OpenUVIndexResponse.serializer(), response.bodyAsText())
    }
}
