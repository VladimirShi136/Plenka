package com.app.plenka.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StringListConverters {
    private val gson = Gson()
    private val type = object : TypeToken<List<String>>() {}.type

    @TypeConverter
    fun fromString(value: String?): List<String> {
        if (value.isNullOrBlank()) return emptyList()
        return try {
            gson.fromJson(value, type) ?: emptyList()
        } catch (_: Exception) {
            // Fallback for legacy split format if any exists
            value.split(";;").map { it.trim() }.filter { it.isNotEmpty() }
        }
    }

    @TypeConverter
    fun toString(list: List<String>?): String {
        if (list.isNullOrEmpty()) return "[]"
        return gson.toJson(list, type)
    }
}
