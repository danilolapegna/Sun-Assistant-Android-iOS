package com.sunassistant.rest.meteo

import com.sunassistant.rest.ApiService
import com.sunassistant.rest.meteo.model.OpenMeteoForecastResponse
import io.ktor.client.statement.bodyAsText

class OpenMeteoForecastService : ApiService() {

    suspend fun fetchWeatherForecast(latitude: Double, longitude: Double): OpenMeteoForecastResponse {
        val params = mutableMapOf(
            "forecast_days" to "7",
            "minutely_15" to "is_day,weather_code,temperature_2m",
            "daily" to "weather_code,sunset,sunrise,uv_index_max,uv_index_clear_sky_max,temperature_2m_max,temperature_2m_min,sunshine_duration"
            // "daily" to "temperature_2m_max,temperature_2m_min" // Uncomment to add daily parameters
            // "current" to "temperature" // Uncomment to add current weather conditions
            // Add other parameters as per requirement
        )

        // Optional parameters
        // params["elevation"] = "100.0" // Set elevation if needed
        // params["temperature_unit"] = "celsius" // Set temperature unit ("celsius" or "fahrenheit")
        // params["wind_speed_unit"] = "kmh" // Set wind speed unit ("kmh", "ms", "mph", "kn")
        // params["precipitation_unit"] = "mm" // Set precipitation unit ("mm" or "inch")
        // params["timeformat"] = "iso8601" // Set time format ("iso8601" or "unixtime")
        // params["timezone"] = "Europe/Berlin" // Set timezone
        // params["forecast_days"] = "7" // Set forecast days (0-16)
        // params["forecast_hours"] = "24" // Set number of forecast hours (>0)
        // params["past_days"] = "0" // Set past days (0-92)
        // params["start_date"] = "2022-06-30" // Set start date for the interval
        // params["end_date"] = "2022-07-07" // Set end date for the interval
        // params["models"] = "auto" // Set weather models ("auto" or specific model names)
        // params["cell_selection"] = "land" // Set cell selection preference ("land", "sea", "nearest")
        // params["apikey"] = "your_api_key" // Set API key for commercial use

        val urlString = OpenMeteoForecastApiConfig.createUrl(latitude, longitude, params)
        val response = apiClient.makeGetRequest(urlString)
        return json.decodeFromString(OpenMeteoForecastResponse.serializer(), response.bodyAsText())
    }
}
