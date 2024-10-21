package com.sunassistant.util

/* A general helper for building settings on both platforms. It has to have default carded list appearance on iOS, full width settings list on
* Android */
object SettingsHelper {

    /* This needs to be as a name to the top of the settings section. It will have a 4dp padding around text top and bottom, while
    * horizontally (left and right) will have to be: CommonDimen.default_padding.dp on Android
     CommonDimen.shared.default_padding on iOS. The text has to be slightly smaller than the items one */
    class SettingsSection(val text: String) {
        private val items = mutableListOf<SettingsItem>()

        fun addSettingItem(item: SettingsItem): SettingsSection {
            items.add(item)
            return this
        }

        fun getSettingsItems(): List<SettingsItem> = items
    }

    /* This is the main method that has to be invoked in order to build the list */
    fun buildSettings(): List<SettingsSection> {

        val uvIndexSettings = SettingsSection("Sources on UV index prevention")
            .addSettingItem(WebUrlSettingItem("Global solar UV index: a practical guide", "https://www.who.int/publications/i/item/9241590076"))
            .addSettingItem(WebUrlSettingItem("WHO recommendations on UV exposure", "https://www.who.int/news-room/fact-sheets/detail/ultraviolet-radiation"))
            .addSettingItem(WebUrlSettingItem("WHO on UV index levels", "https://www.who.int/news-room/questions-and-answers/item/radiation-the-ultraviolet-(uv)-index"))
            .addSettingItem(WebUrlSettingItem("Debunking common myths on UV exposure", "https://news.llu.edu/health-wellness/common-uv-safety-myths-debunked"))
            .addSettingItem(WebUrlSettingItem("Vitamin D: do we need more than sunshine?","https://www.ncbi.nlm.nih.gov/pmc/articles/PMC8299926/#:~:text=It%20is%20optimal%20to%20have,to%20effectively%20make%20vitamin%20D."))
            .addSettingItem(WebUrlSettingItem("Why it is inappropriate to recommend no exposure to sunlight","https://www.mja.com.au/journal/2002/177/11/ultraviolet-radiation-and-health-friend-and-foe"))
            .addSettingItem(WebUrlSettingItem("Burns: a first aid guide","https://medlineplus.gov/ency/article/000030.htm"))

        val vitaminDSettings = SettingsSection("Sources on Vitamin D and UV light benefits")
            .addSettingItem(WebUrlSettingItem("Sunlight and Vitamin D", "https://www.ncbi.nlm.nih.gov/pmc/articles/PMC3897598/"))
            .addSettingItem(WebUrlSettingItem("Benefits and Risks of Sun Exposure to Maintain Adequate Vitamin D Levels", "https://www.ncbi.nlm.nih.gov/pmc/articles/PMC10239563/"))
            .addSettingItem(WebUrlSettingItem("Sun exposure and vitamin D sufficiency", "https://www.sciencedirect.com/science/article/pii/S0002916523241143"))
            .addSettingItem(WebUrlSettingItem("Dietary intake and main food sources of vitamin D as a...", "https://www.ncbi.nlm.nih.gov/pmc/articles/PMC4168313/"))
            .addSettingItem(WebUrlSettingItem("Beneficial effects of UV radiation other than via vitamin D production", "https://www.ncbi.nlm.nih.gov/pmc/articles/PMC3427189/"))

        val airQualitySettings = SettingsSection("Sources on air quality")
            .addSettingItem(WebUrlSettingItem("Air quality, energy and health (WHO)", "https://www.who.int/teams/environment-climate-change-and-health/air-quality-energy-and-health/health-impacts"))
            .addSettingItem(WebUrlSettingItem("Health effects of air pollution: what we need to know and to do", "https://www.ncbi.nlm.nih.gov/pmc/articles/PMC6531724/"))

        val otherSettings = SettingsSection("Other sources")
            .addSettingItem(WebUrlSettingItem("Ultraviolet radiation: health risks and benefits", "https://www.researchgate.net/publication/365488439_Ultraviolet_Radiation_Health_Risks_and_Benefits"))
            .addSettingItem(WebUrlSettingItem("UV radiation and the skin", "https://www.ncbi.nlm.nih.gov/pmc/articles/PMC3709783/"))
            .addSettingItem(WebUrlSettingItem("The effects of solar radiation exposure on human health", "https://link.springer.com/article/10.1007/s43630-023-00375-8"))

        val meteoSourcesSettings = SettingsSection("Meteo sources")
            .addSettingItem(WebUrlSettingItem("DWD Germany", "https://www.dwd.de/EN/Home/home_node.html"))
            .addSettingItem(WebUrlSettingItem("NOAA U.S.", "https://www.noaa.gov/weather"))
            .addSettingItem(WebUrlSettingItem("MeteoFrance", "https://meteofrance.com/"))
            .addSettingItem(WebUrlSettingItem("ECMWF", "https://www.ecmwf.int/"))
            .addSettingItem(WebUrlSettingItem("JMA Japan", "https://www.jma.go.jp/jma/indexe.html"))
            .addSettingItem(WebUrlSettingItem("MET Norway", "https://www.met.no/en"))
            .addSettingItem(WebUrlSettingItem("GEM Canada", "https://weather.gc.ca/canada_e.html"))
            .addSettingItem(WebUrlSettingItem("BOM Australia", "http://www.bom.gov.au/"))
            .addSettingItem(WebUrlSettingItem("CMA China", "http://www.cma.gov.cn/en2014/"))

        val legalsSettings = SettingsSection("Legal & Other")
            .addSettingItem(WebUrlSettingItem("Terms and Conditions", "http://sunassistant.org/terms-conditions/"))
            .addSettingItem(WebUrlSettingItem("Privacy Policy", "http://sunassistant.org/privacy-policy/"))

        return listOf(uvIndexSettings,
            vitaminDSettings,
            airQualitySettings,
            otherSettings,
            meteoSourcesSettings,
            legalsSettings)
    }
}


/* All items should have a padding equal to:
 CommonDimen.default_padding.dp on Android
 CommonDimen.shared.default_padding on iOS */
abstract class SettingsItem {

    abstract val titleText: String

    open val subtitleText: String? = null
}

/* This needs to have text to the left, a switch to the right. If the text reaches the
   switch then it has to ellipsize ant not overlap it.
   The switch is pre-emptively set to initialState action and when switched it triggers the onCheckedChange action */
data class SwitchSettingsItem(
    override val titleText: String,
    val initialState: () -> Boolean = { false },
    val onCheckedChange: (Boolean) -> Unit = {},
    override val subtitleText: String? = null
) : SettingsItem()

/* This needs to have text to the left, default color. If the text reaches the end then it has
   to ellipsize ant not overlap it.
   Clickable and if clicked opens a webview to the url. This opens the url
   a webview if openInBrowser is false, and in browser otherwise */
data class WebUrlSettingItem(
    override val titleText: String,
    val webUrl: String,
    val openInBrowser: Boolean = true,
    override val subtitleText: String? = null
) : SettingsItem()

/* This needs to have text to the left, default color. If the text reaches the end then it has
to ellipsize ant not overlap it.
Clickable and if clicked executes the onClick action */
data class CustomClickSettingsItem(
    override val titleText: String,
    val onClick: () -> Unit,
    override val subtitleText: String? = null
) : SettingsItem()

