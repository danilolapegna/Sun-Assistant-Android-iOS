package com.sunassistant.util

import kotlinx.serialization.Serializable

@Serializable
data class UvIndexReportMessage(val message: String, val messageComponentType: UvIndexReportMessageComponentType)