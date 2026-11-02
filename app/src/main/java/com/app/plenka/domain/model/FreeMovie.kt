package com.app.plenka.domain.model

data class FreeMovie(
    val id: String,
    val title: String,
    val description: String,
    val posterUrl: String,
    val videoUrl: String,
    val license: String,
    val author: String
)
