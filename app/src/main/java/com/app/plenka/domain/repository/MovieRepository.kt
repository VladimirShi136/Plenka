package com.app.plenka.domain.repository

import com.app.plenka.domain.model.Movie

interface MovieRepository {
    suspend fun getPopularMovies(page: Int): Result<List<Movie>>
    suspend fun searchMovies(keyword: String, page: Int): Result<List<Movie>>
}
