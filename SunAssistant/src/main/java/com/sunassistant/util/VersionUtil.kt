package com.sunassistant.util

import java.security.MessageDigest

object VersionUtil {

    const val SAME_VERSION = 0
    const val FIRST_VERSION_BIGGER = 1
    const val SECOND_VERSION_BIGGER = -1

    fun compareVersions(version1: String, version2: String): Int? {
        val regex = Regex("^\\d+\\.\\d+\\.\\d+$")

        if (!regex.matches(version1) || !regex.matches(version2)) {
            return null
        }

        val parts1 = version1.split(".").map { it.toInt() }
        val parts2 = version2.split(".").map { it.toInt() }

        for (i in parts1.indices) {
            if (i >= parts2.size) return FIRST_VERSION_BIGGER // version1 ha più segmenti e non è ancora minore
            if (parts1[i] < parts2[i]) return SECOND_VERSION_BIGGER
            if (parts1[i] > parts2[i]) return FIRST_VERSION_BIGGER
        }

        if (parts1.size < parts2.size) return SECOND_VERSION_BIGGER

        return SAME_VERSION // Le versioni sono uguali
    }

    fun generateUniqueId(vararg inputs: String): String {
        val input = inputs.joinToString("")
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }.substring(0, 32)
    }
}