package com.app.plenka.data.mapper

import com.app.plenka.data.remote.dto.MovieDetailDto
import com.app.plenka.domain.model.MovieDetails

/**
 * Функция для конвертации DTO в movieDetails
 */
fun MovieDetailDto.toDomain(): MovieDetails {
    return MovieDetails(
        id = kinopoiskId ?: filmId ?: 0,
        title = nameRu ?: nameEn ?: nameOriginal ?: "Без названия",
        originalTitle = nameOriginal ?: nameEn,
        year = year,
        posterUrl = posterUrl ?: posterUrlPreview ?: "",
        rating = ratingKinopoisk ?: ratingImdb,
        ratingImdb = ratingImdb,
        genres = genres?.mapNotNull { it.genre } ?: emptyList(),
        description = description ?: shortDescription,
        shortDescription = shortDescription,
        filmLength = filmLength,
        countries = countries?.mapNotNull { it.country } ?: emptyList()
    )
}
