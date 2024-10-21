package com.sunassistant.util

import com.sunassistant.coderesources.CommonStrings_User

object LocationUtils {

    fun latLonString(latitude: Double?, longitude: Double?): String {
        if (latitude == null || longitude == null) return CommonStrings_User.location_unknown
        val roundedLat = (latitude * 100).toInt() / 100.0
        val roundedLon = (longitude * 100).toInt() / 100.0
        return "$roundedLat, $roundedLon"
    }
}