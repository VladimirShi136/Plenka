package com.app.plenka.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.plenka.domain.model.Movie

@Entity(tableName = "favorite_movies")
data class MovieEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val originalTitle: String?,
    val posterUrl: String,
    val posterUrlPreview: String,
    val year: Int?,
    val rating: Double?,
    val genres: String // через запятую
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
        genres = genres.split(", ").filter { it.isNotBlank() }
    )
}

fun Movie.toEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        originalTitle = originalTitle,
        posterUrl = posterUrl,
        posterUrlPreview = posterUrlPreview,
        year = year,
        rating = rating,
        genres = genres.joinToString(", ")
    )
}
