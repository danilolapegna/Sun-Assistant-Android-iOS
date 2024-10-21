package com.sunassistant.coderesources.enums

import com.sunassistant.coderesources.CommonColors
import com.sunassistant.coderesources.CommonStrings_User

enum class SkinType(
    val typeName: String,
    val stringCodeMatch: String,
    val colorReferenceHex: String,
    val typeDescription: String,
    val basicRecommendations: String
) {
    TYPE_1_PALE(
        CommonStrings_User.name_st_1,
        CommonStrings_User.code_st_1,
        CommonColors.skin_type_I,
        CommonStrings_User.description_st_1,
        CommonStrings_User.recommendations_st_1
    ),
    TYPE_2_FAIR(
        CommonStrings_User.name_st_2,
        CommonStrings_User.code_st_2,
        CommonColors.skin_type_II,
        CommonStrings_User.description_st_2,
        CommonStrings_User.recommendations_st_2
    ),
    TYPE_3_MEDIUM(
        CommonStrings_User.name_st_3,
        CommonStrings_User.code_st_3,
        CommonColors.skin_type_III,
        CommonStrings_User.description_st_3,
        CommonStrings_User.recommendations_st_3
    ),
    TYPE_4_OLIVE(
        CommonStrings_User.name_st_4,
        CommonStrings_User.code_st_4,
        CommonColors.skin_type_IV,
        CommonStrings_User.description_st_4,
        CommonStrings_User.recommendations_st_4
    ),
    TYPE_5_BROWN(
        CommonStrings_User.name_st_5,
        CommonStrings_User.code_st_5,
        CommonColors.skin_type_V,
        CommonStrings_User.description_st_5,
        CommonStrings_User.recommendations_st_5
    ),
    TYPE_6_DARK(
        CommonStrings_User.name_st_6,
        CommonStrings_User.code_st_6,
        CommonColors.skin_type_VI,
        CommonStrings_User.description_st_6,
        CommonStrings_User.recommendations_st_6
    );

    fun getShortName() = typeName.split(":").get(0)

    companion object {
        fun getByStringMatch(stringMatch: String?): SkinType? {
            return values().find { it.stringCodeMatch.equals(stringMatch, ignoreCase = true) }
        }
    }
}
