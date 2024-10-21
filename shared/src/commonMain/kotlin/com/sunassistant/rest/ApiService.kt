package com.sunassistant.rest

import kotlinx.serialization.json.Json

open class ApiService {

    open val apiClient = ApiClient()

    open val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
}