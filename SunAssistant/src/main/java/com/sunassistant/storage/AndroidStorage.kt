package com.sunassistant.storage

import android.content.Context

class AndroidStorage(private val context: Context) {
    private val preferences = context.getSharedPreferences("default", Context.MODE_PRIVATE)

    fun save(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    fun get(key: String): String? = preferences.getString(key, null)

    fun save(key: Float, value: Float) {
        preferences.edit().putFloat(key.toString(), value).apply()
    }

    fun get(key: Float): Float? = preferences.getFloat(key.toString(), -1f).takeIf { it != -1f }

    fun save(key: Boolean, value: Boolean) {
        preferences.edit().putBoolean(key.toString(), value).apply()
    }

    fun get(key: Boolean): Boolean = preferences.getBoolean(key.toString(), false)
}
