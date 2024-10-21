package com.sunassistant.util

enum class AirQualityLevel(val description: String, val highRisk: Boolean, val colorHexString: String) {
    UNKNOWN("Unknown", false, "#9E9E9E"),
    GOOD("Good", false, "#006400"),
    FAIR("Fair", false, "#6B8E23"),
    UNHEALTHY_IF_SENSITIVE("Poor if sensitive", false, "#FFD700"),
    UNHEALTHY("Poor", true, "#FF8C00"),
    VERY_UNHEALTHY("Very poor", true, "#FF4500"),
    HAZARDOUS("Hazardous", true, "#8B0000");

    companion object {
        fun getLevelByIndex(index: Double?, isUS: Boolean): AirQualityLevel {
            return if (isUS) {
                when {
                    index == null -> UNKNOWN
                    index <= 50 -> GOOD
                    index <= 100 -> FAIR
                    index <= 150 -> UNHEALTHY_IF_SENSITIVE
                    index <= 200 -> UNHEALTHY
                    index <= 300 -> VERY_UNHEALTHY
                    else -> HAZARDOUS
                }
            } else {
                when {
                    index == null -> UNKNOWN
                    index <= 20 -> GOOD
                    index <= 40 -> FAIR
                    index <= 60 -> UNHEALTHY_IF_SENSITIVE
                    index <= 80 -> UNHEALTHY
                    index <= 100 -> VERY_UNHEALTHY
                    else -> HAZARDOUS
                }
            }
        }
    }
}