package com.sunassistant.coderesources

/*
 * Objects for resources that may be reachable from all modules, Android or iOS
 */

object CommonColors {

    //General colors
    const val theme_color_accent_background = "#fff1c2"
    const val theme_color_accent_background_lighter = "#fff7dc"

    const val theme_color_dark = "#02030b"

    const val skin_type_I = "#ffe5cc"
    const val skin_type_II = "#fcd2a7"
    const val skin_type_III = "#e3b78e"
    const val skin_type_IV = "#bf9270"
    const val skin_type_V = "#825e42"
    const val skin_type_VI = "#4a3321"

    const val darker_green = "#006400"
    const val darker_yellow = "#a37802"
    const val darker_orange = "#943c01"
    const val darker_red = "#8B0000"
    const val darker_purple = "#301934"

    const val black = "#000000"
    const val white = "#FFFFFF"
    const val dark_gray_1 = "#404040"
    const val dark_gray_2 = "#333333"
    const val transparent = "#FFFFFFFF"

    const val sunassistant_green = "#c5dfe0"
    const val sunassistant_orange = "#faeacf"
    const val sunassistant_beige_lighter = "#fcf9f7"
    const val sunassistant_beige = "#fcf4ef"
    const val sunassistant_beige_darker = "#fcede3"
    const val sunassistant_blue = "#162f3a"

    //Theme colors
    const val toolbar_content_color = white
    const val toolbar_background_color = sunassistant_blue

    const val bottom_navbar_background_color = sunassistant_beige
    const val bottom_navbar_content_color_unselected = black
    const val bottom_navbar_content_color_selected = dark_gray_2

    const val content_card_background_color = sunassistant_beige_lighter

    const val theme_color_background = sunassistant_beige

    const val background_screen_color = sunassistant_beige

}

object CommonStrings_User {

    const val button_confirm = "Confirm"

    const val progress_loading = "Loading..."

    const val name_st_1 = "Very Fair"
    const val name_st_2 = "Fair"
    const val name_st_3 = "Medium"
    const val name_st_4 = "Olive"
    const val name_st_5 = "Brown"
    const val name_st_6 = "Dark"

    const val code_st_1 = "st1"
    const val code_st_2 = "st2"
    const val code_st_3 = "st3"
    const val code_st_4 = "st4"
    const val code_st_5 = "st5"
    const val code_st_6 = "st6"

    const val description_st_1 = "Melano-compromised. Always burns, rarely tans."
    const val description_st_2 = "Melano-compromised. Usually burns, sometimes tans."
    const val description_st_3 = "Melano-competent. Sometimes burns, usually tans."
    const val description_st_4 = "Melano-competent. Rarely burns, always tans."
    const val description_st_5 = "Melano-protected. Rarely burns."
    const val description_st_6 = "Melano-protected. Rarely burns."

    const val skin_selection_title = "Which skin type is closer to yours?"
    const val skin_selection_subtitle =
        "This is just an estimate that will help us to give you better recommendations. UV index measurement will still be considered the most important factor and a professional assessment on your risk level will still be necessary."

    const val uv_index_retrieval_error = "Impossible to understand UV index in your location. Sorry"
    const val location_retrieval_error = "Impossible to retrieve location."
    const val forecast_retrieval_error = "UV forecast still unavailable. Please try again later."

    const val location_authorization_denied =
        "You need to enable location services if you want to check UV index and forecast in your area."

    const val uv_index_subcircular = "Estimated UVI in your area"

    const val button_change_skin_type = "Edit Skin Type"

    const val location_unknown = "Unknown"

    const val general_unknown = "Unknown"

    var recommendations_st_1 = """
    Melano-compromised skin (Very Fair): 
    - 🔬  See a professional for precise assessment of your individual skin risks.
    - 🚫  Highly susceptible to sunburn.
    - 🧴  Always use broad-spectrum sunscreen with high SPF, possibly 50+.
    - 👒  Wear wide-brimmed hats and UV-protection sunglasses.
    - 🌳  Seek shade during peak sun hours (and check what's the peak sun hours of the day in the "Forecast" section).
""".trimIndent()

    var recommendations_st_2 = """
    Melano-compromised skin (Fair): 
    - 🔬  See a professional for precise assessment of your individual skin risks.
    - 🔥  High likelihood of sunburn.
    - 🧴  Apply broad-spectrum sunscreen with high SPF.
    - 🕶️  Use protective clothing and seek shade when outdoors.
    - ⏰  Limit direct sun exposure to avoid skin damage.
    - 💦  Reapply sunscreen every two hours or after swimming/sweating.
""".trimIndent()

    var recommendations_st_3 = """
    Melano-competent skin (Medium): 
    - 🔬  See a professional for precise assessment of your individual skin risks.
    - 🌞  Moderate risk of sunburn.
    - 🧴  Use sunscreen during prolonged outdoor activities.
    - 👒 🕶️ Wear hats and sunglasses for additional protection.
    - 🌴  Find shade during midday hours when the sun is strongest.
    - 🔍  Monitor skin changes and adapt sun exposure accordingly.
""".trimIndent()

    var recommendations_st_4 = """
    Melano-competent skin (Olive): 
    - 🔬  See a professional for precise assessment of your individual skin risks.
    - 🍂  Lower risks of sunburn or skin cancer, but still at risk.
    - 🧴  Recommended to use sunscreen.
    - 🕶️  Protective clothing and sunglasses are beneficial.
    - ☀️  Be aware of sun exposure during peak hours.
    - 🔎  Regularly check skin for any unusual changes.
""".trimIndent()

    var recommendations_st_5 = """
    Melano-protected skin (Brown): 
    - 🔬  See a professional for precise assessment of your individual skin risks.
    - 🌥️  Reduced risk of sunburn, but sun protection is still important.
    - 🧴  Use sunscreen in intense sun conditions.
    - 🧢  Protective clothing can help prevent long-term skin damage.
    - 💧  Stay hydrated and monitor sun exposure duration.
    - 🩺  Regular skin examinations are important for health.
""".trimIndent()

    var recommendations_st_6 = """
    Melano-protected skin (Dark): 
    - 🔬  See a professional for a more precise assessment of your individual skin risks.
    - 💪  Natural protection against sunburn, but not immune to sun damage or skin cancer.
    - 🧴  Sunscreen with SPF 15+ is advisable for prolonged exposure.
    - 🕶️🧢  Sunglasses and hats add layers of protection.
    - ⏳  Pay attention to the duration of sun exposure.
    - 🏥  Regular skin health check-ups are recommended.
""".trimIndent()

    const val uv_index_unknown =
        "Cannot retrieve UV Index in your area. Check that the app has all the correct location permissions from your settings. In meantime, use standard sun protection and cover at peak hours."

    const val uv_index_unknown_forecast_short =
        "Cannot retrieve UV Index for this day, yet."

    const val uv_index_night_0 =
        "\uD83C\uDF1F  Good evening! As the sun has set, the UV index is now at its lowest, giving you a well-deserved break from the sun's rays. Remember, daytime will soon return, so let's plan ahead! Check tomorrow's UV forecast for safe sun enjoyment and keep your sunscreen handy for when the sun comes up. Have a sweet evening and take care!"

    const val uv_index_night_1 =
        "\uD83C\uDF0C  Good night! The UV index is zero, so no worries about sun exposure now. Enjoy your restful evening and recharge for a sunny tomorrow. Don't forget to check the UV levels in the morning! Sweet dreams!"

    const val uv_index_night_2 =
        "\uD83C\uDF20  The stars are out and the UV index is down! It's a great time to relax under the night sky. Remember, the sun will be back, so let's get ready for another day of safe sun fun. Have a lovely night!"

    const val uv_index_night_3 =
        "\uD83C\uDF1F  Night has fallen and UV concerns are gone! Enjoy the peaceful, starry hours and get some well-earned rest. We'll keep an eye on tomorrow's UV forecast for you. Good night and sweet dreams!"

    const val uv_index_night_4 =
        "\uD83D\uDD6F\uFE0F  No UV rays to worry about tonight! It's a perfect opportunity to unwind and relax. Tomorrow's another sunny day, so let's stay prepared. Sleep well and take care!"

    const val uv_index_night_5 =
        "\uD83C\uDF20  A UV-free night is here! Time to enjoy the tranquility of the evening. Let's check the UV forecast in the morning for another day of sun safety. Have a great night and rest easy!"

    const val uv_index_night_6 =
        "\uD83C\uDF1C  The moon's out and UV levels are at a low. Take this time to relax and enjoy the night. We'll be here to update you on tomorrow's UV index. Have a peaceful evening!"

    const val uv_index_night_7 =
        "\uD83C\uDF1C  UV rays are taking a break, and so should you! Enjoy the calmness of the night. We'll keep you posted on UV changes. Rest well and see you in the morning!"

    const val uv_index_night_8 =
        "\uD83D\uDECB\uFE0F  A UV-free evening is upon us! No need for sunscreen now, but stay prepared for tomorrow. Enjoy a quiet, restful night and take good care of yourself!"

    const val uv_index_night_9 =
        "\uD83C\uDF0C  As the night sky brightens with stars, UV worries fade away. It's the perfect time for a cozy evening. We'll keep an eye on the UV for tomorrow. Have a beautiful night!"

    const val unknown_error = "An error while trying to retrieve data. Please try again."

    private var randomNightMessageCache: String? = null

    val your_skin_type = "Your skin type"

    val tips_card_title = "☀\uFE0F Sunny tips ☀\uFE0F"

    fun getRandomNightUvMessage(): String {
        val messages = listOf(
            uv_index_night_0,
            uv_index_night_1,
            uv_index_night_2,
            uv_index_night_3,
            uv_index_night_4,
            uv_index_night_5,
            uv_index_night_6,
            uv_index_night_7,
            uv_index_night_8,
            uv_index_night_9
        )
        if (randomNightMessageCache != null) return randomNightMessageCache ?: messages.random()
        randomNightMessageCache = messages.random()
        return randomNightMessageCache ?: messages.random()
    }
}

object CommonStrings_System {
    const val sunassistant_url = "https://www.sunassistant.org"
    const val open_uv_key = "{Insert your openUV key here}"

    const val home_menu_label = "Assistant"
    const val forecast_menu_label = "Forecast"
    const val store_menu_label = "About us"
    const val settings_menu_label = "Info" //Will be settings once we have settings
}

object CommonStrings_Keys {
    const val SKIN_TYPE_KEY = "skin_type_key"
}

object CommonDimen {

    const val padding_4 = 4.0
    const val padding_8 = 8.0
    const val padding_16 = 16.0
    const val padding_18 = 18.0
    const val padding_20 = 20.0
    const val padding_24 = 24.0
    const val padding_32 = 32.0
    const val padding_48 = 48.0
    const val padding_56 = 56.0
    const val padding_64 = 64.0

    const val text_size_8 = 8.0
    const val text_size_10 = 10.0
    const val text_size_11 = 11.0
    const val text_size_12 = 12.0
    const val text_size_14 = 14.0
    const val text_size_16 = 16.0
    const val text_size_18 = 18.0
    const val text_size_20 = 20.0
    const val text_size_22 = 22.0
    const val text_size_24 = 24.0
    const val text_size_32 = 32.0
    const val text_size_36 = 36.0
    const val text_size_42 = 42.0
    const val text_size_48 = 48.0
    const val text_size_56 = 56.0

    //TODO: bind to theme
    /* Default */

    const val text_medium_small = text_size_12
    const val text_default = text_size_14
    const val text_medium_large = text_size_16
    const val text_large = text_size_18
    const val text_extra_large = text_size_32

    const val default_padding = padding_16

    const val default_height_100 = 100.0
    const val default_height_150 = 150.0
    const val default_height_250 = 250.0
    const val default_height_300 = 300.0

    const val default_corner_radius = 10.0

    const val circle_counter_size = 200.0
}
