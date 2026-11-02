package com.app.plenka.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO модель расширенного описания фильма
 */
data class MovieDetailDto(
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
    @SerializedName("genres") val genres: List<GenreDto>?,
    @SerializedName("description") val description: String?,
    @SerializedName("shortDescription") val shortDescription: String?,
    @SerializedName("filmLength") val filmLength: Int?,
    @SerializedName("countries") val countries: List<CountryDto>?
)

data class CountryDto(
    @SerializedName("country") val country: String?
)
