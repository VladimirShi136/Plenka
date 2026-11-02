package com.app.plenka.data.mapper

import com.app.plenka.data.remote.dto.MovieDto
import com.app.plenka.domain.model.Movie

/**
 * Функция для конвертации DTO в movie
 */
fun MovieDto.toDomain(): Movie {
    return Movie(
        id = this.kinopoiskId ?: this.filmId ?: 0,
        title = this.nameRu ?: this.nameEn ?: this.nameOriginal ?: "Без названия",
        originalTitle = this.nameOriginal ?: this.nameEn,
        year = this.year,
        posterUrl = this.posterUrl ?: "",
        posterUrlPreview = this.posterUrlPreview ?: this.posterUrl ?: "",
        rating = this.ratingKinopoisk ?: this.ratingImdb,
        genres = this.genres?.mapNotNull { it.genre } ?: emptyList()
    )
}
