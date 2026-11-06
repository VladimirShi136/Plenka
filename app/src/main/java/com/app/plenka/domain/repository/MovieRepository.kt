package com.app.plenka.domain.repository

import com.app.plenka.domain.model.Movie
import com.app.plenka.domain.model.MovieDetails

import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс репозитория
 */
interface MovieRepository {
    suspend fun getPopularMovies(page: Int): Result<List<Movie>>
    suspend fun searchMovies(keyword: String, page: Int): Result<List<Movie>>
    suspend fun getMovieDetails(filmId: Int): Result<MovieDetails>

    fun getFavoriteMovies(): Flow<List<Movie>>
    fun observeIsFavorite(movieId: Int): Flow<Boolean>
    suspend fun isFavorite(movieId: Int): Boolean
    suspend fun toggleFavorite(movie: Movie)
}
