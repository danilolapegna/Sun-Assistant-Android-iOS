package com.sunassistant.util

import com.sunassistant.coderesources.enums.SkinType

object Adviceopedia {

    internal fun generateHealthySunTips(uvIndex: Double?): String {
        val adviceListMorning = listOf(
            "🌞  Morning sunlight kickstarts your serotonin levels, improving mood and focus for the day ahead.",
            "⏰  Exposure to early sunlight can significantly enhance your circadian rhythm, promoting healthier sleep patterns.",
            "💪  A brief morning sunbath boosts Vitamin D synthesis, crucial for bone health and immune function.",
            "👓  Sunlight in the morning reduces the risk of myopia in children by encouraging healthy eye development.",
            "❤️  Early sun exposure is linked to lower blood pressure and improved cardiovascular health.",
            "⚖️  Morning light exposure may help in weight management by regulating metabolic rates.",
            "🌱  Spend time in the morning sun to enhance skin health through natural disinfection.",
            "🧠  A morning routine involving sunlight can stimulate your brain, enhancing cognitive functions.",
            "😌  Exposure to sunlight after waking up can decrease feelings of grogginess, thanks to a reduction in melatonin production.",
            "😃  Morning sunlight exposure can lead to better mental health and reduced symptoms of depression.",
            "🌤️  The ultraviolet light in the morning sun can help treat certain skin conditions, like psoriasis.",
            "🔋  Starting your day with sunlight can increase your energy levels, setting a positive tone for the day.",
            "🍃  Early morning sun exposure enhances the body's synthesis of essential nutrients, including improving overall nutrient absorption.",
            "🧬  Exposure to morning sunlight helps regulate melatonin production for the evening, supporting a restful night's sleep.",
            "👁️  Regular exposure to morning sunlight can reduce the risk of seasonal affective disorder (SAD), a type of depression related to changes in seasons.",
            "🚶‍  Morning sunlight exposure is linked to increased physical activity throughout the day by boosting energy and motivation.",
            "📉  Morning sunlight exposure can lead to lower cortisol levels, reducing stress and anxiety.",
            "🧘‍  Morning sunlight enhances focus during meditation and mindfulness practices, promoting a calming environment.",
            "🏃‍  Exposure to morning sunlight can improve athletic performance by increasing alertness and warming up the body.",
            "📚  Sunlight exposure in the morning has been shown to improve cognitive functions, such as learning and memory.",
            "🌿  Morning sunlight supports the body's detoxification processes, promoting liver health and aiding in toxin elimination.",
            "💆‍  Incorporating morning sunlight into a wellness routine can improve skin health, including elasticity and appearance.",
            "🛌  Natural morning light exposure helps regulate sleep hormones, aiding in waking up refreshed.",
            "🎨  Morning light provides optimal natural lighting for artistic and creative activities, enhancing color perception.",
            "🍲  Morning sunlight exposure may positively influence eating habits by improving mood and reducing cravings for unhealthy foods.",
            "🕊️  Safely practiced early morning sun gazing may contribute to spiritual well-being and inner peace."
        )

        val adviceListAfternoon = listOf(
            "🍽️  Afternoon sunlight helps regulate leptin levels, aiding in appetite control.",
            "🌅  Viewing the sunset frequencies primes your body for a restful night by adjusting your sleep cycle.",
            "😊  Late afternoon sun exposure can provide a mood boost, helping to combat afternoon slumps.",
            "\uD83C\uDF05  Exposure to sunlight in the late afternoon supports Vitamin D synthesis without the intense midday UV rays.",
            "🏞️  Outdoor activities in the late afternoon sun can lower stress and increase relaxation.",
            "👁️  The softer light in the afternoon is easier on the eyes and can reduce the risk of developing cataracts.",
            "❤️  Sunlight exposure before sunset can improve heart health by lowering blood pressure.",
            "🌞  The warm afternoon sun has anti-inflammatory effects, beneficial for skin and joint health.",
            "🕰️  Sunlight later in the day supports the natural rhythms of your body, enhancing overall well-being.",
            "⏰  Late-day sunlight exposure can help in adjusting your body clock, especially if you experience jet lag.",
            "📸  The golden hour light is perfect for photography, promoting creativity and mental health through art.",
            "😄  Spending time in the sunlight during the late afternoon can enhance endorphin production, hence boosting happiness.",
            "🏃‍  Afternoon sunlight exposure may enhance muscle recovery post-exercise due to its anti-inflammatory properties.",
            "🧠  Exposure to natural light in the afternoon can help maintain circadian rhythms, critical for cognitive function and alertness.",
            "🌡️  Moderate exposure to afternoon sunlight may help regulate body temperature, aiding in metabolic efficiency.",
            "💡  Afternoon sunlight exposure supports the synthesis of nitric oxide, which can improve blood circulation and oxygenation.",
            "🎯  Spending time in the afternoon sunlight can enhance focus and productivity by reducing the hormone melatonin, which induces sleep.",
            "🌺  Moderate exposure to the afternoon sun may boost the immune system by activating T cells, which help fight infection.",
            "📊  Afternoon sun exposure is linked to a reduction in the risk of certain autoimmune diseases, through modulation of the immune system.",
            "🛡️  The filtered UV rays in the afternoon can aid in skin repair and rejuvenation, contributing to a healthier skin appearance.",
            "💤  Exposure to sunlight in the afternoon has been associated with better sleep quality by helping regulate the sleep-wake cycle.",
            "👩‍🔬  Afternoon sunlight can improve insulin sensitivity, potentially reducing the risk of diabetes by regulating blood sugar levels.",
            "📈  Regular, moderate exposure to afternoon sunlight is linked to lower rates of several types of cancer, due to vitamin D's protective effects.",
            "🧩  Afternoon sun exposure can support mental health by potentially lowering the risk of developing mood disorders.",
            "🚴  Light physical activity in the afternoon sunlight can increase the production of proteins involved in muscle growth and repair.",
            "🔋  Absorbing sunlight in the afternoon may boost mitochondrial function, enhancing energy production at the cellular level."
        )

        val genericSunAdvice = listOf(
            "❤️  Sunlight exposure is crucial for Vitamin D synthesis, which supports bone health and immune function.",
            "😊  Regular sun exposure can improve mood and fight off seasonal depression by boosting serotonin levels.",
            "🦠  UV rays from the sun have natural antibacterial properties that can benefit your skin health.",
            "🌙  Sunlight helps regulate the production of melatonin, leading to better sleep quality.",
            "😄  Exposure to sunlight can increase the production of endorphins, boosting your overall mood.",
            "❤️  Sunlight plays a role in lowering blood pressure through the release of nitric oxide from the skin.",
            "👀  Sun exposure is linked to improved eye health, reducing the risk of nearsightedness in children and adolescents.",
            "❤\uFE0F  Regular, moderate sun exposure may decrease the risk of certain cancers, including colon, kidney, and breast cancer.",
            "🔄  Natural sunlight exposure is essential for maintaining the circadian rhythm and promoting healthy sleep patterns.",
            "💡  Sunlight exposure can enhance cognitive function by improving alertness and focus.",
            "🌞  Exposure to the sun's UV rays can help treat several skin conditions, including acne, psoriasis, and eczema.",
            "🛡️  Moderate sun exposure can boost the immune system by increasing white blood cell count, which helps fight infections.",
            "🧵  Exposure to sunlight can stimulate the production of collagen in the skin, promoting skin elasticity and reducing the appearance of aging.",
            "🌡️  Sunlight exposure helps regulate body temperature through effects on the skin and endocrine system, enhancing thermal comfort and metabolic rate.",
            "🍽️  Sunlight influences hormonal balance, including hormones related to hunger and satiety, potentially aiding in weight management.",
            "🔋  Sunlight exposure can boost mitochondrial function, enhancing energy production in cells and contributing to overall vitality.",
            "🚶‍  Regular sunlight exposure is associated with increased physical activity, encouraging a healthier lifestyle and physical fitness.",
            "💊  Sunlight can influence the efficacy of certain medications, affecting how the body metabolizes drugs and thus impacting their effectiveness.",
            "🧠  Exposure to sunlight has been shown to enhance neuroplasticity, the brain's ability to form new connections, improving learning and memory.",
            "📉  Sun exposure can have a regulatory effect on autoimmune diseases by modulating the immune system and reducing inflammation.",
            "💞  Sun exposure has been linked to improved heart health, as UV rays can help reduce cholesterol levels by converting it to vitamin D.",
            "🧘  Regular sunlight exposure can enhance mindfulness and meditation practices by promoting relaxation and a sense of well-being.",
            "🔬  Sunlight exposure is critical for the synthesis of certain hormones and neurochemicals beyond serotonin, including melatonin and dopamine, influencing mood and sleep cycles."
        )

        val currentTime = UVAssistant.getCurrentHour()

        val finalMessage = when {
            currentTime in 9..12 -> adviceListMorning.random()
            currentTime in 15..19 -> adviceListAfternoon.random()
            else -> genericSunAdvice.random()
        }

        return finalMessage
    }
    internal fun generateSkinTypeTip(
        skinType: SkinType
    ): String {
        val lightSkinTypeAdvices = listOf(
            "🧴  Light skin types are at increased risk for sunburn even under low UV conditions; always apply a high SPF sunscreen.",
            "🧴  For those with very light skin, sunscreen should be a non-negotiable part of your daily skincare routine, regardless of the season.",
            "🚗  Remember, UV rays can penetrate glass. If you've got very light skin, consider UV protection on your car and home windows.",
            "👂  Light skin types may require extra attention to sunscreen application, when necessary, on often-missed areas like neck, ears, and scalp.",
            "🔍  Given your skin type, be vigilant about new or changing moles and consult with a dermatologist annually for a skin check.",
            "🧪  Use after-sun products containing aloe vera or soy to soothe sunburned skin, which is more prevalent among light skin types.",
            "👚  Select clothing with UPF (Ultraviolet Protection Factor) rating to wear during outdoor activities for added sun protection.",
            "🌱  Investigate more delicate sunscreens if you have sensitive skin; these will be less likely to cause skin irritation while still offering effective UV protection.",
            "🍊  Incorporate vitamin C and E in your diet or skincare routine to boost your skin's UV protection naturally.",
            "🛑  Avoid tanning beds as they can increase the risk of skin cancer and premature aging, particularly for light skin types.",
            "📅  Schedule routine dermatological exams to monitor skin health and detect any early signs of skin cancer.",
            "🏊‍  Remember to apply water-resistant sunscreen before swimming and reapply every two hours or immediately after swimming or sweating.",
            "🔬  Research has shown that individuals with light skin have a higher risk of UV-induced DNA damage, highlighting the importance of physical UV barriers like hats and sunglasses.",
            "🧴  Consider using sunscreens with ingredients like zinc oxide or titanium dioxide for broad-spectrum protection without chemical absorbers, which may be preferable for sensitive skin.",
            "🥕  Eating foods rich in beta-carotene has been shown to provide some protection against sunburn, particularly valuable for light skin types.",
            "🚫  Be aware of the reflection of UV rays off surfaces like water, sand, and snow, which can increase exposure and risk of sunburn even in cooler temperatures.",
            "🕵️‍♂️  Use wearable UV index monitors to stay informed about the sun's intensity in real time, allowing for more precise sun protection measures.",
            "🌞  Consider vitamin D supplementation, as extensive sun protection measures may lead to vitamin D deficiency, particularly in individuals with light skin living in higher latitudes.",
            "🧖‍  Invest in skincare products that repair and strengthen the skin barrier, such as those containing ceramides, to mitigate the effects of UV exposure.",
            "💡  Utilize UV-protective films for home and office windows to reduce cumulative sun exposure, crucial for light skin types prone to cumulative sun damage over time.",
            "🛌  Adopt a nighttime skincare routine aimed at repairing UV damage with products containing ingredients like retinol, which can help reverse some effects of sun exposure."
        )

        val darkSkinTypeAdvices = listOf(
            "🌞  While darker skin types have more natural protection against UV radiation, sunburn and damage can still occur.",
            "🧴  Use a broad-spectrum sunscreen to protect against both UVA and UVB rays, even if your skin seldom burns.",
            "🔍  Darker skin can mask signs of sun damage. Pay attention to changes in skin texture or hyperpigmentation.",
            "😎  Sunglasses with UV protection are essential for eye health, regardless of how dark your skin may be.",
            "👩‍  Annual dermatologist visits for skin checks are crucial, as darker skin types can develop melanoma in less visible areas.",
            "🍏  Consider using skincare products with antioxidants to fight free radical damage from sun exposure.",
            "🥥  Use natural oils with SPF properties, like coconut oil, for light protection during short periods of exposure, but don't rely solely on these as your primary sun protection method.",
            "🌿  Explore the use of products with niacinamide and licorice extract to address hyperpigmentation and even out skin tone.",
            "🛌  Implement nighttime routines that include products with retinol or peptides to support skin repair and combat signs of premature aging.",
            "🧪  Be mindful of using products containing hydroquinone under professional guidance, as improper use can lead to skin discoloration, especially on darker skin.",
            "🧖‍  Regularly moisturize your skin with lotions enriched with shea butter or hyaluronic acid to maintain hydration and elasticity.",
            "📚  Educate yourself about the specific risks of keloid formation in darker skin types and consult with dermatologists about prevention and treatment.",
            "💆‍  Seek professional advice before undergoing cosmetic procedures, as darker skin is more prone to post-inflammatory hyperpigmentation.",
            "🍇  Integrate foods high in polyphenols, such as dark chocolate and berries, into your diet to enhance skin's resilience against UV damage.",
            "🔬  Be aware of pseudofolliculitis barbae, a condition more common in darker skin due to curly hair, and adopt appropriate shaving techniques to prevent it.",
            "🤲  Use gentle, non-abrasive exfoliants to remove dead skin cells and improve skin texture without causing irritation or damage.",
            "🌱  Incorporate topical antioxidants like vitamin C serums in your skincare routine to boost collagen production and mitigate UV-induced damage.",
            "👟  Wear protective clothing and accessories, such as long-sleeved shirts and wide-brimmed hats, when spending extended periods outdoors.",
            "🚰  Stay hydrated and consume a balanced diet rich in vitamins and minerals to support overall skin health and function.",
            "⏰  Establish a consistent skincare routine, both morning and night, to address the unique needs of your skin type and ensure its health and vitality."
        )

        val mediumSkinTypeAdvices = listOf(
            "⚖️  Medium skin types balance between burn and tan but must still practice diligent sun protection.",
            "🧴  Given your skin type, using a sunscreen with an SPF of 30 or higher is a good practice for daily wear, even on cloudy days.",
            "🔍  Monitor your skin for any changes, such as new growths or changes in existing moles, and consult a healthcare provider with concerns."
        )

        val genericAdvices = listOf(
            "🍇  Photoprotection is not just about lotions; dietary antioxidants can also support your skin's defense against UV damage.",
            "💧  Hydration is one of the keys for maintaining skin health; ensure you drink ample water if you spend a lot of time outdoors.",
            "💋  Consider wearing UV-protective lip balm to protect your lips, which are also vulnerable to sun damage.",
            "👕  Clothing with UV protection can significantly reduce your risk of UV damage.",
            "💊  Be conscious of medications that may increase your sensitivity to sunlight, including certain antibiotics and acne treatments.",
            "⏲️  In case of swimming or excessive sweating, immediately reapply your sunscreen.",
            "👒  A hat with at least a 2-3 inch brim could be recommended to protect the face, ears, and back of the neck.",
            "🛡️  Consider using skincare products with antioxidants to fight free radical damage from sun exposure.",
            "😎  UV-blocking sunglasses help prevent cataracts and protect the more delicate skin around your eyes.",
            "🌳  Sunscreen isn't just for the beach; apply it for any extended outdoor activity, including gardening or hiking.",
            "🍅  Increase your intake of lycopene-rich foods like tomatoes and watermelon, which can naturally increase your skin's SPF.",
            "🔬  Understand your skin's needs by getting a professional skin assessment, which can guide your choice of sun protection and skincare products.",
            "👧  Educate children early about the importance of sun protection to instill lifelong healthy habits.",
            "🔒  Make a habit of checking product expiration dates, as expired sunscreens can lose their effectiveness and expose your skin to potential harm.",
            "💻  Use screen time as an opportunity to take a break from UV exposure, emphasizing indoor activities during peak sun hours as part of a balanced approach to UV safety.",
            "🎨  Explore creative shading options for outdoor areas, such as UV-blocking umbrellas, pergolas, and awnings, to enhance livable spaces while minimizing UV risk.",
            "🌬️  Protect your skin from windburn and dehydration by applying a barrier cream when engaging in winter sports or activities in cold, windy conditions.",
            "🏋️‍♂️  Exercise regularly to improve circulation and support skin health, enhancing its natural ability to repair damage from environmental stressors.",
            "🧘‍  Practice stress-reduction techniques such as yoga or meditation, as stress can weaken the skin barrier and increase vulnerability to UV damage.",
            "🚿  Limit hot showers, which can strip the skin of its natural oils, and follow up with a moisturizer to lock in hydration.",
            "🌾  Consider using products with ceramides or hyaluronic acid to maintain skin's moisture barrier, especially after sun exposure.",
            "🚫  Avoid smoking, which accelerates skin aging and undermines its natural protection against environmental damage, including UV rays.",
            "🥤  Boost your intake of omega-3 fatty acids from fish, nuts, and seeds to strengthen skin's lipid barrier and enhance its resilience to UV damage.",
            "🌺  Use sunscreens that contain antioxidants such as vitamin E or green tea extract, which can provide additional protection against free radical damage induced by UV exposure.",
            "🔵  Opt for blue-light filtering glasses when using electronic devices for prolonged periods, as blue light can contribute to skin and eye aging similar to prolonged UV exposure.",
            "🏞️  If you're in an area with high pollution, increase your skin's defense with sunscreens that also offer protection against environmental pollutants, as pollution can exacerbate UV damage.",
            "👶  For infants and young children, use physical barriers such as sun-protective clothing and hats, as their skin is more sensitive to chemical sunscreens and UV damage.",
            "🔧  Keep a UV protection kit in your car or bag, including a broad-spectrum sunscreen, UV-blocking sunglasses, a wide-brimmed hat, and a portable shade umbrella, ensuring you're always prepared for sun exposure."
        )

        val lightSkinTypeAdvice = (lightSkinTypeAdvices + genericAdvices).random()
        val darkSkinTypeAdvice = (darkSkinTypeAdvices + genericAdvices).random()
        val mediumSkinTypeAdvice = (mediumSkinTypeAdvices + genericAdvices).random()

        val skinTypeAdvice = "\n\n" +
                when (skinType) {
                    SkinType.TYPE_1_PALE, SkinType.TYPE_2_FAIR -> lightSkinTypeAdvice
                    SkinType.TYPE_5_BROWN, SkinType.TYPE_6_DARK -> darkSkinTypeAdvice
                    else -> mediumSkinTypeAdvice
                }

        return skinTypeAdvice
    }

}