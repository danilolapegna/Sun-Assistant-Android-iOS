package com.sunassistant.coderesources.enums;

enum class WeatherType(vararg val stringMatches: String) {
    RAIN("rain"),
    SLEET("sleet"),
    SNOW("snow"),
    CLEAR("clear", "sunny"),
    PARTLY_CLOUDY("partly cloudy", "partly cloudy"),
    CLOUDY("cloudy"),
    FOG("fog"),
    UNKNOWN("unknown");

    companion object {
        fun getByStringMatch(weatherString: String): WeatherType {
            val match = values().find { weatherType ->
                weatherType.stringMatches.any { match -> weatherString.contains(match, ignoreCase = true) }
            }
            return match ?: UNKNOWN
        }
    }
}
