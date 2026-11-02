package com.app.plenka.domain.model

/**
 * Краткая модель фильма для списков
 */
data class Movie(
    val id: Int,
    val title: String,
    val originalTitle: String?,
    val year: Int?,
    val posterUrl: String,
    val posterUrlPreview: String,
    val rating: Double?,
    val genres: List<String>
)
