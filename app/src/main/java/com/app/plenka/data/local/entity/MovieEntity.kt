package com.app.plenka.data.local.entity

import androidx.room.Entity
import com.app.plenka.domain.model.Movie

// Составной ключ: один и тот же фильм может лежать в кэше и «популярных», и в результатах поиска.
@Entity(tableName = "movies_cache", primaryKeys = ["id", "category"])
data class MovieEntity(
    val id: Int,
    val title: String,
    val originalTitle: String?,
    val posterUrl: String,
    val posterUrlPreview: String,
    val year: Int?,
    val rating: Double?,
    val genres: List<String>, // список жанров через TypeConverter
    val category: String, // "popular" или "search_{keyword}"
    val page: Int
)

fun MovieEntity.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        originalTitle = originalTitle,
        posterUrl = posterUrl,
        posterUrlPreview = posterUrlPreview,
        year = year,
        rating = rating,
        genres = genres
    )
}

fun Movie.toEntity(category: String, page: Int): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        originalTitle = originalTitle,
        posterUrl = posterUrl,
        posterUrlPreview = posterUrlPreview,
        year = year,
        rating = rating,
        genres = genres,
        category = category,
        page = page
    )
}
