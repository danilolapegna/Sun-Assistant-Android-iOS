package com.sunassistant.rest

import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.HttpRequestBuilder

class ApiClient {

    var permanentHeaders: Map<String, String>? = null

    val httpClient = HttpClient {
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
    }

    fun HttpRequestBuilder.applyPermanentHeaders() {
        permanentHeaders?.forEach { (key, value) ->
            headers.append(key, value)
        }
    }

    suspend fun makeGetRequest(url: String): HttpResponse {
        return httpClient.get(url) {
            applyPermanentHeaders()
        }
    }

    suspend inline fun <reified T> makePostRequest(url: String, body: T): HttpResponse {
        return httpClient.post(url) {
            applyPermanentHeaders()
            setBody(body)
        }
    }

    suspend inline fun <reified T> makePutRequest(url: String, body: T): HttpResponse {
        return httpClient.put(url) {
            applyPermanentHeaders()
            setBody(body)
        }
    }

    suspend inline fun <reified T> makePatchRequest(url: String, body: T): HttpResponse {
        return httpClient.patch(url) {
            applyPermanentHeaders()
            setBody(body)
        }
    }

    suspend fun makeDeleteRequest(url: String): HttpResponse {
        return httpClient.delete(url) {
            applyPermanentHeaders()
        }
    }
}
