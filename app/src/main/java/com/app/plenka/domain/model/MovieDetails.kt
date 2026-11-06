package com.app.plenka.domain.model

/**
 * Полная модель фильма для экрана детального просмотра
 */
data class MovieDetails(
    val id: Int,
    val title: String,
    val originalTitle: String?,
    val year: Int?,
    val posterUrl: String,
    val rating: Double?,
    val ratingImdb: Double?,
    val genres: List<String>,
    val description: String?,
    val shortDescription: String?,
    val filmLength: Int?,
    val countries: List<String>,
    val trailerUrl: String?,
    val youtubeTrailerUrl: String?
)
