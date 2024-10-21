package com.sunassistant.util;

import com.sunassistant.coderesources.CommonColors
import com.sunassistant.coderesources.CommonStrings_User
import com.sunassistant.coderesources.CommonStrings_User.getRandomNightUvMessage
import com.sunassistant.coderesources.enums.SkinType
import com.sunassistant.rest.meteo.model.UvIndexDailyForecast
import com.sunassistant.util.Adviceopedia.generateHealthySunTips
import com.sunassistant.util.Adviceopedia.generateSkinTypeTip
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.TimeZone
import kotlin.math.roundToInt

object UVAssistant {

    fun getUvIndexReportMessageStructured(
        uvIndex: Double?,
        skinType: SkinType,
        weatherCode: Int? = null,
        isDay: Boolean? = null,
        airQualityLevel: AirQualityLevel? = null,
    ): List<UvIndexReportMessage> {
        val messages = mutableListOf<UvIndexReportMessage>()
        val statusString =
            inferUVIndexMessage(
                uvIndex,
                weatherCode,
                skinType,
                null,
                airQualityLevel,
                isDay = isDay
            )

        messages.add(
            UvIndexReportMessage(
                statusString,
                UvIndexReportMessageComponentType.UV_STATUS
            )
        )
        messages.add(
            UvIndexReportMessage(
                getSunTips(
                    uvIndex,
                    skinType
                ),
                UvIndexReportMessageComponentType.VITAMIN_D_RECOMMENDATIONS
            )
        )

        messages.add(
            UvIndexReportMessage(
                skinType.basicRecommendations,
                UvIndexReportMessageComponentType.SKIN_TYPE
            )
        )
        messages.add(
            UvIndexReportMessage(
                "Bright surfaces such as sand, water, and snow, will increase UV exposure. Extra precautions are needed for children, seniors, and those who have greater sun sensitivity. For any concerns, always consult a certified physician.",
                UvIndexReportMessageComponentType.EXTRA
            )
        )
        return messages.filter { it.message.isNotBlank() }
    }

    fun getUvIndexReportPlainText(
        uvIndex: Double?,
        skinType: SkinType,
        weatherCode: Int? = null,
        isDay: Boolean? = null,
        airQualityLevel: AirQualityLevel? = null
    ): String {

        return getUvIndexReportMessageStructured(
            uvIndex,
            skinType,
            weatherCode,
            isDay,
            airQualityLevel
        )
            .map { it.message }
            .filter { it.isNotBlank() }
            .joinToString("\n\n")
    }

    private fun isMostlySunny(weatherCode: Int): Boolean {
        return weatherCode <= 2
    }

    private fun getSunTips(
        uvIndex: Double?,
        skinType: SkinType
    ): String {

        val healthySunTips1 = inferHealthySunTips(uvIndex)
        val healthySunTips2 = generateSkinTypeTip(skinType = skinType)

        return buildString {

            if (healthySunTips1.isNotBlank()) {
                append(healthySunTips1)
            }
            if (healthySunTips2.isNotBlank()) {
                append(healthySunTips2)
            }
            if ((uvIndex != null && uvIndex >= 3.0) || skinType == SkinType.TYPE_1_PALE || skinType == SkinType.TYPE_2_FAIR) {
                append("\n\n⚠\uFE0F  Don't forget that all recommendations on healthy sun exposure will still need you to prioritise sun safety.")
            }
        }
    }

    internal fun getCurrentHour(): Int {
        val currentMoment = Clock.System.now()
        val currentDateTime = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())
        return currentDateTime.hour
    }

    open class AdviceCache(
        val adviceMessage: String,
        val adviceTime: Instant,
        val cacheExpireInMills: Long = 2000L /* 2 seconds default */
    )

    private var healthySunnyTips: AdviceCache? = null

    private fun inferHealthySunTips(uvIndex: Double?): String {
        val now = Clock.System.now()
        healthySunnyTips?.let {
            if (it.adviceMessage.isNotBlank() && (now.toEpochMilliseconds() - it.adviceTime.toEpochMilliseconds()) < it.cacheExpireInMills) {
                return it.adviceMessage
            }
        }
        val newAdvice = generateHealthySunTips(uvIndex)
        healthySunnyTips = AdviceCache(newAdvice, now)
        return newAdvice
    }

    private fun generateAirQualityAdvice(
        uvIndex: Double?,
        airQualityLevel: AirQualityLevel?,
        isDay: Boolean?
    ): String {
        val advice = when (airQualityLevel) {
            AirQualityLevel.GOOD -> "\uD83C\uDF33  " + run {
                if (isDay != true) "The air quality is good, which means it's a great time for outdoor activities (if weather allows to). Enjoy!" else when (uvIndex) {
                    null -> "The air quality is good, which means it's a great time for outdoor activities. Enjoy nature, but also don't forget basic sun protection measures!"
                    in 0.0..<3.0 -> "The air quality is good, which means it's a great time for outdoor activities. Enjoy nature!"
                    in 3.0..<6.0 -> "The air quality is good, ideal for outdoor activities. However, given the moderate UV index, take some sun protection measures."
                    in 6.0..<8.0 -> "The air quality is good, but if you plan any outdoor activities ensure you protect yourself against the high UV levels."
                    in 8.0..<11.0 -> "The air quality is good. Outdoor activities are appealing, but very high UV levels still demand significant sun protection."
                    else -> "The air quality is good. However, with extreme UV levels, it's crucial to minimize direct sun exposure and adopt maximum protection if outside."
                }
            }

            AirQualityLevel.FAIR -> "\uD83C\uDF3F  " + run {
                if (isDay != true) "The air quality is fair, suitable for most outdoor activities. Enjoy your time outside (if weather allows to)|" else when (uvIndex) {
                    null,
                    in 0.0..<3.0 -> "The air quality is fair, suitable for most outdoor activities. Enjoy your time outside with basic precautions."
                    in 3.0..<6.0 -> "The air quality is fair. While that makes outdoor activities still safe air-wise, don't forget to consider moderate UV protection measures."
                    in 6.0..<8.0 -> "The air quality is fair. Outdoor activities are okay, but high UV levels mean extra sun protection is important."
                    in 8.0..<11.0 -> "The air quality is fair. Still, it's wise to be cautious of very high UV levels by seeking shade and wearing protective clothing."
                    else -> "The air quality is fair. However, extreme UV levels require minimizing outdoor activities and ensuring maximum sun protection."
                }
            }

            AirQualityLevel.UNHEALTHY_IF_SENSITIVE -> "\uD83D\uDCA8  " + run {
                if (isDay != true) "The air quality is unhealthy for sensitive groups. Limit prolonged outdoor activities, especially if you're part of these groups." else when (uvIndex) {
                    null,
                    in 0.0..<3.0 -> "The air quality is unhealthy for sensitive groups. Those affected should be cautious."
                    in 3.0..<6.0 -> "The air quality is unhealthy for sensitive groups. Limit prolonged outdoor activities and apply moderate sun protection."
                    in 6.0..<8.0 -> "The air quality is unhealthy for sensitive groups. It's advisable to reduce time outside and protect against high UV exposure."
                    in 8.0..<11.0 -> "The air quality is unhealthy for sensitive groups. Very high UV levels further necessitate limiting outdoor exposure and using strong sun protection."
                    else -> "The air quality is unhealthy for sensitive groups. Extreme UV levels demand minimizing outdoor activities and adopting maximum protection measures."
                }
            }

            AirQualityLevel.UNHEALTHY -> "\uD83D\uDE37  " + run {
                if (isDay != true) "The air quality is unhealthy. Outdoor activities should be limited." else when (uvIndex) {
                    null -> "The air quality is unhealthy. Outdoor activities should be limited."
                    in 0.0..<3.0 -> "The air quality is unhealthy. Outdoor activities should be limited; if outside, low UV levels allow for some leniency with sun protection."
                    in 3.0..<6.0 -> "The air quality is unhealthy. Everyone should reduce outdoor activities, with added caution due to moderate UV levels."
                    in 6.0..<8.0 -> "The air quality is unhealthy. It is advisable to limit outdoor exposure and take significant precautions against high UV radiation."
                    in 8.0..<11.0 -> "The air quality is unhealthy. Minimize outdoor activities and be extremely cautious of very high UV levels, prioritizing protective measures."
                    else -> "The air quality is unhealthy. Outdoor exposure should be minimized; extreme UV levels necessitate utmost caution and protective actions if outside."
                }
            }

            AirQualityLevel.VERY_UNHEALTHY -> "\uD83D\uDEAB  " + run {
                if (isDay != true) "The air quality is very unhealthy. Avoiding outdoor activities is crucial." else when (uvIndex) {
                    null,
                    in 0.0..<3.0 -> "The air quality is very unhealthy. Avoiding outdoor activities is crucial."

                    in 3.0..<6.0 -> "The air quality is very unhealthy. It's important to stay indoors, and moderate UV levels further support this advice."
                    in 6.0..<8.0 -> "The air quality is very unhealthy. Outdoor activities should be avoided, with high UV adding to the urgency for protection."
                    in 8.0..<11.0 -> "The air quality is very unhealthy. Staying indoors becomes even more critical with the additional risk from very high UV levels."
                    else -> "The air quality is very unhealthy. The necessity to avoid outdoor activities is amplified by extreme UV levels, underscoring the need for maximum precautions."
                }
            }

            AirQualityLevel.HAZARDOUS -> "☠\uFE0F  " + run {
                if (isDay != true) "The air quality is hazardous. It's crucial to stay indoors and avoid outdoor activities due to the extreme air pollution risk." else when (uvIndex) {
                    null -> "The air quality is hazardous. It's crucial to stay indoors and avoid outdoor activities due to the extreme air pollution risk."
                    in 0.0..<3.0 -> "The air quality is hazardous. Despite the low UV index, it's crucial to stay indoors and avoid outdoor activities due to the extreme air pollution risk."
                    in 3.0..<6.0 -> "The air quality is hazardous. Although the UV index is moderate, outdoor activities are strongly discouraged due to the severe air quality conditions."
                    in 6.0..<8.0 -> "The air quality is hazardous. High UV levels further compound the risks, but the primary concern remains the hazardous air quality, necessitating staying indoors."
                    in 8.0..<11.0 -> "The air quality is hazardous. Very high UV levels should not distract from the critical need to avoid outdoor exposure due to the dangerous air pollution."
                    else -> "The air quality is hazardous. Extreme UV levels are a secondary concern to the dire air quality situation, which demands staying indoors and limiting exposure."
                }
            }

            AirQualityLevel.UNKNOWN,
            null -> "\uD83D\uDD0D  Air quality is unknown at the moment. If you plan spending time outside, remain alert to further news and to your body signals."
        }
        return advice
    }

    class UvAdviceCache(adviceMessage: String, adviceTime: Instant, val uvIndex: Double?) :
        AdviceCache(adviceMessage, adviceTime)

    private var uvIndexAdviceCache: UvAdviceCache? = null

    private fun inferUVIndexMessage(
        uvIndex: Double?,
        weatherCode: Int?,
        skinType: SkinType,
        temperature: Double? = null,
        airQualityLevel: AirQualityLevel? = null,
        isDay: Boolean? = null
    ): String {
        val now = Clock.System.now()
        uvIndexAdviceCache?.let {
            if (it.uvIndex != null && it.adviceMessage.isNotBlank() && (now.toEpochMilliseconds() - it.adviceTime.toEpochMilliseconds()) < it.cacheExpireInMills) {
                return it.adviceMessage
            }
        }
        val newAdvice =
            generateUVIndexMessage(
                uvIndex,
                weatherCode,
                temperature,
                airQualityLevel,
                isDay,
                skinType
            )
        uvIndexAdviceCache = UvAdviceCache(newAdvice, now, uvIndex)
        return newAdvice
    }

    fun generateShortUVIndexMessage(
        uvIndex: Double?,
        weatherCode: Int?
    ): String {
        if (uvIndex == null) return CommonStrings_User.uv_index_unknown_forecast_short
        val shortUvAdvice = when (uvIndex) {
            in 0.0..<3.0 -> "Low UV: minimal risk. Enjoy outdoors safely."
            in 3.0..<6.0 -> "Moderate UV: seek shade, wear protective clothing, and use sunscreen during peak hours."
            in 6.0..<8.0 -> "High UV: increased skin damage risk. Apply high SPF sunscreen, wear hats during peak hours."
            in 8.0..<11.0 -> "Very high UV: intense precautions needed. Use broad-spectrum sunscreen, sunglasses, stay shaded during peak hours."
            else -> "Extreme UV: maximum protection required. Avoid exposure, cover up, use high SPF sunscreen during peak hours."
        }

        val shortWeatherAdvice = when (weatherCode) {
            in 70..79, 85, 86 -> " Consider also that snow and ice may increase UV reflection."
            in 10..49 -> " Remember also that clouds don't fully block UV."
            else -> ""
        }

        return buildString {
            append(shortUvAdvice)
            if (shortWeatherAdvice.isNotEmpty()) append("$shortWeatherAdvice ")
        }
    }

    fun generateDetailedUvIndexForecastAdvice(
        todayDetailedForecastItems: MutableList<UvIndexDailyForecast>?,
        sunrise: String?,
        sunset: String?,
        weatherCode: Int?
    ): String {
        val defaultAdvice =
            CommonStrings_User.uv_index_unknown_forecast_short + " More detailed advice could be available later."

        return analyzeDaylightUvIndex(
            todayDetailedForecastItems,
            sunrise?.toInstantOrNull(),
            sunset?.toInstantOrNull(),
            defaultAdvice,
            weatherCode
        )
    }

    internal fun getDayOnlyTimes(
        todayDetailedForecastItems: MutableList<UvIndexDailyForecast>?,
        sunrise: Instant?,
        sunset: Instant?
    ): List<UvIndexDailyForecast> {

        val timeZone = TimeZone.currentSystemDefault()
        return todayDetailedForecastItems?.filter {
            val hour = it.dailyDate.toLocalDateTime(timeZone)
            if (sunrise != null && sunset != null) {
                val sunriseLocalDateTime = sunrise.toLocalDateTime(timeZone)
                val sunsetLocalDateTime = sunset.toLocalDateTime(timeZone)

                ((((it.uvIndex
                    ?: 0.0) > 0) && (hour >= sunriseLocalDateTime) && (hour <= sunsetLocalDateTime)))
            } else {
                (((it.uvIndex ?: 0.0) > 0))
            }

        }?.sortedBy { it.dailyDate } ?: listOf()
    }

    private fun analyzeDaylightUvIndex(
        todayDetailedForecastItems: MutableList<UvIndexDailyForecast>?,
        sunrise: Instant?,
        sunset: Instant?,
        defaultAdvice: String,
        weatherCode: Int?
    ): String {
        if (todayDetailedForecastItems.isNullOrEmpty()) return defaultAdvice
        val daylightUvForecasts = getDayOnlyTimes(todayDetailedForecastItems, sunrise, sunset)
        if (daylightUvForecasts.isEmpty()) return defaultAdvice

        val riskBands = mutableListOf<List<UvIndexDailyForecast>>()
        var currentBand = mutableListOf<UvIndexDailyForecast>()

        if (daylightUvForecasts.isNotEmpty()) {
            currentBand.add(daylightUvForecasts.first())
        }

        for (i in 1 until daylightUvForecasts.size) {
            val current = daylightUvForecasts[i]
            val last = currentBand.last()

            if (getBandUvIndexRiskLevel(current.uvIndex) == getBandUvIndexRiskLevel(last.uvIndex)) {
                currentBand.add(current)
            } else {
                riskBands.add(currentBand)
                currentBand = mutableListOf(current)
            }
        }

        riskBands.add(currentBand)

        return constructRiskBandMessage(riskBands, sunrise, sunset, weatherCode)
    }

    enum class BandUvIndexRisk(val emoji: String, val advice: String) {
        UNKNOWN("❓", ", which gives us not enough information to give proper recommendations."),
        LOW(
            "🟢",
            ", making it great for safe sun exposure (if weather, temperature and air quality allow to)."
        ),
        MODERATE("🟡", ". Protective clothing, sunglasses, and sunscreen are advisable."),
        HIGH(
            "🟠",
            ". High SPF sunscreen, hats, sunglasses, and limiting sun exposure are strongly recommended."
        ),
        EXTREMELY_HIGH(
            "🔴",
            ". Avoid direct sun exposure whenever possible, and use strong precautions (hats, sunglasses, protective clothing, high SPF sunscreen) otherwise."
        );
    }

    private fun getBandUvIndexRiskLevel(uvIndex: Double?): BandUvIndexRisk {
        return when (uvIndex) {
            null -> BandUvIndexRisk.UNKNOWN
            in Double.MIN_VALUE..<3.0 -> BandUvIndexRisk.LOW
            in 3.0..<6.0 -> BandUvIndexRisk.MODERATE
            in 6.0..<8.0 -> BandUvIndexRisk.HIGH
            else -> BandUvIndexRisk.EXTREMELY_HIGH
        }
    }

    private fun constructRiskBandMessage(
        riskBands: MutableList<List<UvIndexDailyForecast>>,
        sunrise: Instant?,
        sunset: Instant?,
        weatherCode: Int?
    ): String {
        var atLeastOneBandHasNonLowRisk = false
        val riskBandDefaultMessage = riskBands.joinToString("\n") { band ->
            val startHour = band.first().dailyDate
            val endHour = band.last().dailyDate
            val riskLevel = getBandUvIndexRiskLevel(band.first().uvIndex)
            val riskCategory = riskLevel.name.lowercase().replace("_", " ")
            if (riskLevel != BandUvIndexRisk.LOW && riskLevel != BandUvIndexRisk.UNKNOWN) {
                atLeastOneBandHasNonLowRisk = true
            }
            val riskEmoji = riskLevel.emoji
            val advice = riskLevel.advice
            if (riskBands.size == 1) {
                if (sunrise != null && sunset != null) {
                    riskEmoji + "  The whole day (from ${sunrise.formatBasicTimeOnly()} to ${sunset.formatBasicTimeOnly()}) should be within the $riskCategory UV risk category$advice"
                } else {
                    "$riskEmoji  The whole day should be within the $riskCategory UV risk category. $advice"
                }

                /* Last band has sunset in it */
            } else if (band == riskBands.last()) {
                if (sunset != null) {
                    if (startHour != sunset) {
                        riskEmoji + "  From approximately ${startHour.formatBasicTimeOnly()} until sunset at ${sunset.formatBasicTimeOnly()}, the UV risk should be $riskCategory$advice"
                    } else {
                        riskEmoji + "  Around sunset at ${sunset.formatBasicTimeOnly()}, the UV risk should be $riskCategory$advice"
                    }
                } else {
                    riskEmoji + "  From approximately ${startHour.formatBasicTimeOnly()} until sunset, the UV risk should be $riskCategory$advice"
                }

                /* First band has sunrise in it */
            } else if (band == riskBands.first()) {
                if (sunrise != null) {
                    if (endHour != sunrise) {
                        riskEmoji + "  From sunrise at ${sunrise.formatBasicTimeOnly()}, till around ${endHour.formatBasicTimeOnly()}, the UV risk should be $riskCategory$advice" + "\n"
                    } else {
                        riskEmoji + "  Around sunrise at ${sunrise.formatBasicTimeOnly()}, the UV risk should be $riskCategory$advice" + "\n"
                    }
                } else {
                    riskEmoji + "  From sunrise until ${endHour.formatBasicTimeOnly()}, the UV risk should be $riskCategory$advice" + "\n"
                }
            } else {
                if (startHour == endHour) {
                    riskEmoji + "  Around ${startHour.formatBasicTimeOnly()} the UV risk should be $riskCategory$advice" + "\n"
                } else {
                    riskEmoji + "  From approximately ${startHour.formatBasicTimeOnly()} to ${endHour.formatBasicTimeOnly()}, the UV risk should be $riskCategory$advice" + "\n"
                }
            }
        }
        val additionalWeatherMessage =
            if (atLeastOneBandHasNonLowRisk && weatherCode != null && !isMostlySunny(weatherCode)) {
                "\n\n⚠\uFE0F  Remember: clouds do not fully block UV radiation."
            } else ""
        return riskBandDefaultMessage + additionalWeatherMessage
    }

    private fun generateUVIndexMessage(
        uvIndex: Double?,
        weatherCode: Int?,
        temperature: Double? = null,
        airQualityLevel: AirQualityLevel? = null,
        isDay: Boolean? = null,
        skinType: SkinType,
    ): String {

        val uvAdvice = when (uvIndex) {
            null -> CommonStrings_User.uv_index_unknown
            in 0.0..<1.0 -> "🟢  UV index is very low: minimal risk from sun exposure. There's little or no sun now, but if you can still see some natural light, try to expose your skin and eyes to it (if air quality and weather allow) in order to improve your mood and optimise your hormones release."
            in 1.0..<3.0 -> "🟢  UV index is low: minimal risk from unprotected sun exposure. If weather allows, it is an ideal time for a brief sun exposure (5-25 minutes) to enhance sun health benefits, like mood regulation, and hormone optimisation."
            in 3.0..<6.0 -> "🟡  UV index is medium: moderate risk of harm from the sun. It's wise to seek shade 🌳, wear sun-protective clothing 👕👒. If you have to stay outside or you want to have some healthy sun exposure, stay 5-10 minutes at max, use sunscreen 🧴 and listen to your body signals."
            in 6.0..<8.0 -> "🟠  UV index is high: increased risk of skin damage. Protective measures like using sunscreen with high SPF 🧴, wearing hats 🧢, and limiting sun exposure are strongly recommended."
            in 8.0..<11.0 -> "🔴  UV index is very high: intense sun safety precautions necessary, like broad-spectrum sunscreen 🧴, sunglasses \uD83D\uDD76\uFE0F, and staying in the shade as much as possible."
            else -> "\uD83D\uDFE3  An extreme UV index calls for maximum protection. Avoid sun exposure if possible, and otherwise cover up 🧣🧤, and regularly apply a high SPF sunscreen 🧴. Remember, the risk of harm is significant."
        }

        val snowAdvice = when (weatherCode) {
            in 70..79, 85, 86 -> "Be cautious as snow ❄️ can reflect UV radiation, increasing general risk. Consider additional sun protection measures."
            else -> ""
        }

        val cloudAdvice = if (uvIndex != null && uvIndex > 3.0 && weatherCode != null) when {
            !isMostlySunny(weatherCode) -> "Clouds ☁️ do not fully block UV radiation, so still use all the necessary precautions."
            else -> ""
        } else {
            ""
        }

        val skinAdvice = if (uvIndex != null && uvIndex < 3.0 && (skinType == SkinType.TYPE_1_PALE || skinType == SkinType.TYPE_2_FAIR)) {
            "Also, the most sensitive skin types always require additional care."
        } else if (uvIndex != null && uvIndex >= 3.0 && (skinType == SkinType.TYPE_1_PALE || skinType == SkinType.TYPE_2_FAIR)) {
            "Also, the most sensitive skin types will require even extra precautions and care."
        } else if (uvIndex != null && uvIndex >= 3.0 && (skinType == SkinType.TYPE_5_BROWN || skinType == SkinType.TYPE_6_DARK)) {
            "Even highly pigmented skin, despite more resistant to high UV radiation, requires protection."
        } else {
            ""
        }

        val temperatureAdvice =
            if (uvIndex != null && temperature != null && temperature < 20 && uvIndex > 3.0)
                "And don't be misled by cooler temperatures ❄️; sunburn can still occur." else ""

        val airQualityAdvice = generateAirQualityAdvice(uvIndex, airQualityLevel, isDay)

        return buildString {
            if (isDay == true) {
                append(uvAdvice)
                if (snowAdvice.isNotEmpty()) append(" $snowAdvice")
                if (cloudAdvice.isNotEmpty()) append(" $cloudAdvice")
                if (temperatureAdvice.isNotEmpty()) append(" $temperatureAdvice")
                if (skinAdvice.isNotEmpty()) append(" $skinAdvice")
            } else if (isDay == false) {
                append(getRandomNightUvMessage())
            } else {
                append(CommonStrings_User.uv_index_unknown)
            }
            if (airQualityAdvice.isNotEmpty()) append("\n\n$airQualityAdvice ")
        }
    }

    private fun getAirQualityString(index: Double?, isUS: Boolean): String {
        return when (index) {
            null -> "(${AirQualityLevel.UNKNOWN.description})"
            else -> {
                val formattedIndex = formatDoubleToTwoDigits(index)
                val level = AirQualityLevel.getLevelByIndex(index, isUS)
                "$formattedIndex (${level.description})"
            }
        }
    }

    fun getMinMaxTemperatureString(temperatureMax: String?, temperatureMin: String?): String {
        val minTempString = getTemperatureString(temperatureMin)
        val maxTempString = getTemperatureString(temperatureMax)
        return "$minTempString to $maxTempString".trim(' ', '/')
    }

    // Overloaded method for String input
    fun getTemperatureString(temperature: String?): String {
        // Convert String to Double, if possible, and call the Double variant
        return temperature?.toDoubleOrNull()?.let {
            getTemperatureString(it)
        } ?: "Unknown"
    }

    fun getTemperatureString(temperature: Double?): String {
        if (temperature == null) return "Unknown"
        val temperatureF = celsiusToFahrenheit(temperature)
        return "$temperature°C ($temperatureF°F)"
    }

    fun celsiusToFahrenheit(celsius: String?): String? {
        return celsius?.toDoubleOrNull()?.let {
            celsiusToFahrenheit(it)
        }
    }

    fun celsiusToFahrenheit(celsius: Double): String {
        val fahrenheit = celsius * 9 / 5 + 32
        // Manually round to one decimal place
        return ((fahrenheit * 10).toInt() / 10.0).toString()
    }


    fun getTemperatureColor(tempCelsius: Double?): String {
        return when {
            tempCelsius == null -> "#000000" // Default to black if temperature is unknown or parsing fails
            tempCelsius < 0 -> "#0A47A0" // Darker Blue for very cold
            tempCelsius < 5 -> "#0D52BF" // Dark Blue for cold
            tempCelsius < 10 -> "#3470D3" // Medium Blue for cool
            tempCelsius < 15 -> "#5591F2" // Lighter Blue for slightly cool
            tempCelsius < 20 -> CommonColors.darker_yellow // Dark Yellow for mild
            tempCelsius < 25 -> "#F29F05" // Orange for warm
            tempCelsius < 30 -> CommonColors.darker_orange // Darker Orange for hot
            tempCelsius < 35 -> "#F24405" // Red-Orange for very hot
            else -> "#D81120" // Dark Red for extreme heat
        }
    }

    fun getTemperatureColor(temperature: String?): String {
        val tempCelsius = temperature?.toDoubleOrNull()
        return getTemperatureColor(tempCelsius)
    }

    fun getSunsetSunriseString(sunset: String?, sunrise: String?): String {
        val unknownTimeString = "unknown time"
        val sunriseTime = sunrise?.let {
            try {
                LocalDateTime.parse(it).hour.toString()
                    .padStart(2, '0') + ":" + LocalDateTime.parse(it).minute.toString()
                    .padStart(2, '0')
            } catch (e: Exception) {
                unknownTimeString
            }
        } ?: unknownTimeString

        val sunsetTime = sunset?.let {
            try {
                LocalDateTime.parse(it).hour.toString()
                    .padStart(2, '0') + ":" + LocalDateTime.parse(it).minute.toString()
                    .padStart(2, '0')
            } catch (e: Exception) {
                unknownTimeString
            }
        } ?: unknownTimeString

        return "Sun rises at $sunriseTime, sets at $sunsetTime."
    }


    fun getUSAirQualityString(index: Double?): String {
        return getAirQualityString(index, true)
    }

    fun getEUAirQualityString(index: Double?): String {
        return getAirQualityString(index, false)
    }

    fun formatDoubleToTwoDigits(value: Double): String {
        val scaled = (value * 100).roundToInt()
        val result = scaled.toDouble() / 100.0
        return result.toString()
    }

    fun getWeatherDescription(weatherCode: Int?): String {
        return when (weatherCode) {
            null -> "Weather condition is unknown."
            0 -> "Clear sky."
            1 -> "Mainly clear sky."
            2 -> "Partly cloudy sky."
            3 -> "Overcast sky."
            45 -> "Fog."
            48 -> "Depositing rime fog."
            51 -> "Light drizzle."
            53 -> "Moderate drizzle."
            55 -> "Dense intensity drizzle."
            56 -> "Light freezing drizzle."
            57 -> "Dense freezing drizzle."
            61 -> "Slight rain."
            63 -> "Moderate rain."
            65 -> "Heavy intensity rain."
            66 -> "Light freezing rain."
            67 -> "Heavy freezing rain."
            71 -> "Slight snowfall."
            73 -> "Moderate snowfall."
            75 -> "Heavy intensity snowfall."
            77 -> "Snow grains are present."
            80 -> "Slight rain showers."
            81 -> "Moderate rain showers."
            82 -> "Violent rain showers."
            85 -> "Slight snow showers."
            86 -> "Heavy snow showers."
            95 -> "Thunderstorm of slight or moderate intensity."
            96 -> "Thunderstorm with slight hail."
            99 -> "Thunderstorm with heavy hail."
            else -> "unknown/unspecified weather."
        }
    }

    fun getWeatherForecast(weatherCode: Int?): String {
        val description = getWeatherDescription(weatherCode)
        val lowercaseDescription =
            description.replaceFirstChar { if (it.isUpperCase()) it.lowercase() else it.toString() }
        return "Expectation of $lowercaseDescription"
    }

    fun getWeatherEmoji(weatherCode: Int?, nightEmojis: Boolean = false): String {
        return when (weatherCode) {
            0 -> if (nightEmojis) "🌙" else "☀️" // Clear sky
            in 1..3 -> if (nightEmojis) "☁️" else "⛅" // Mainly clear, partly cloudy, and overcast
            45, 48 -> if (nightEmojis) "☁" else "⛅" // Fog and depositing rime fog
            in 51..55 -> if (nightEmojis) "🌧️" else "🌦️" // Drizzle
            56, 57 -> if (nightEmojis) "🌧️" else "🌦️" // Freezing Drizzle
            in 61..65 -> if (nightEmojis) "🌧️" else "🌧️" // Rain
            66, 67 -> if (nightEmojis) "🌧️" else "🌨️" // Freezing Rain
            in 71..75 -> "❄️" // Snow fall
            77 -> "🌨️" // Snow grains
            in 80..82 -> if (nightEmojis) "🌧️" else "🌦️" // Rain showers
            85, 86 -> "🌨️" // Snow showers
            95 -> "⛈️" // Thunderstorm: Slight or moderate
            96, 99 -> "⛈️" // Thunderstorm with hail
            else -> "\uD83C\uDF0E" // Unknown or out of range values
        }
    }

    fun colorForValueString(value: Float?): String {
        if (value == null) return "#ffffff"
        return when (value) {
            in 0f..<3f -> CommonColors.darker_green
            in 3f..<6f -> CommonColors.darker_yellow
            in 6f..<8f -> CommonColors.darker_orange
            in 8f..10f -> CommonColors.darker_red
            else -> CommonColors.darker_purple
        }
    }
}
