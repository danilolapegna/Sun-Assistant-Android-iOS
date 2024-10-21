package com.sunassistant.rest.meteo

import kotlinx.datetime.Clock


abstract class AbstractUVIndexResponse {

    abstract fun uvIndexCurrent(): Double?

    abstract fun getUvIndexFetchedTimeInEpochMs(): Long?
}

data class ConcreteUVIndexResponse(
    private val uvIndex: Double? = null,
    private val uvIndexFetchedTimeInEpochMs: Long? = Clock.System.now().toEpochMilliseconds()
) : AbstractUVIndexResponse() {

    override fun uvIndexCurrent(): Double? = uvIndex

    override fun getUvIndexFetchedTimeInEpochMs(): Long? = uvIndexFetchedTimeInEpochMs
}