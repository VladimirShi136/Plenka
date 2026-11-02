package com.app.plenka.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("total") val total: Int?,
    @SerializedName("totalPages") val totalPages: Int?,
    @SerializedName("items") val items: List<MovieDto>?
)

/**
 * DTO краткое описание карточки фильма на главной странице
 */
data class MovieDto(
    @SerializedName("kinopoiskId") val kinopoiskId: Int?,
    @SerializedName("filmId") val filmId: Int?,
    @SerializedName("nameRu") val nameRu: String?,
    @SerializedName("nameEn") val nameEn: String?,
    @SerializedName("nameOriginal") val nameOriginal: String?,
    @SerializedName("year") val year: Int?,
    @SerializedName("posterUrl") val posterUrl: String?,
    @SerializedName("posterUrlPreview") val posterUrlPreview: String?,
    @SerializedName("ratingKinopoisk") val ratingKinopoisk: Double?,
    @SerializedName("ratingImdb") val ratingImdb: Double?,
    @SerializedName("genres") val genres: List<GenreDto>?
)

data class GenreDto(
    @SerializedName("genre") val genre: String?
)
