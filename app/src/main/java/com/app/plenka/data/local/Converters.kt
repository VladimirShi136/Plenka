package com.app.plenka.data.local

import androidx.room.TypeConverter

class StringListConverters {
    @TypeConverter
    fun fromString(value: String?): List<String> {
        if (value.isNullOrBlank()) return emptyList()
        // Сериализуем через JSON-подобный простой формат или экранирование, 
        // либо разделяем по символу, поддерживая экранирование запятых (или просто храним список через JSON / разделяем по специальному разделителю)
        // Намного надежнее хранить список через JSON-массив (Gson) или разделитель типа `;;` вместо запятой.
        // Давай использовать разделитель `;;`, так как в названиях жанров запятые почти не встречаются, но `;;` полностью исключает коллизии.
        return value.split(";;").map { it.trim() }.filter { it.isNotEmpty() }
    }

    @TypeConverter
    fun toString(list: List<String>?): String {
        if (list.isNullOrEmpty()) return ""
        return list.joinToString(";;")
    }
}
