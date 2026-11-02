package com.app.plenka.domain.repository

import com.app.plenka.domain.model.Movie
import com.app.plenka.domain.model.MovieDetails

/**
 * Интерфейс репозитория
 */
interface MovieRepository {
    suspend fun getPopularMovies(page: Int): Result<List<Movie>>
    suspend fun searchMovies(keyword: String, page: Int): Result<List<Movie>>
    suspend fun getMovieDetails(filmId: Int): Result<MovieDetails>
}

