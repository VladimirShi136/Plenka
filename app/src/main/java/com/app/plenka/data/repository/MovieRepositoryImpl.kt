package com.app.plenka.data.repository

import com.app.plenka.BuildConfig
import com.app.plenka.data.mapper.toDomain
import com.app.plenka.data.remote.KinopoiskApi
import com.app.plenka.domain.model.Movie
import com.app.plenka.domain.repository.MovieRepository
import javax.inject.Inject

/**
 * Реализация интерфейса репозитория
 */
class MovieRepositoryImpl @Inject constructor(
    private val api: KinopoiskApi
) : MovieRepository {

    private val apiKey: String
        get() = BuildConfig.KP_API_KEY

    override suspend fun getPopularMovies(page: Int): Result<List<Movie>> {
        return try {
            val response = api.getCollections(apiKey = apiKey, type = "TOP_POPULAR_MOVIES", page = page)
            val movies = response.items?.map { it.toDomain() } ?: emptyList()
            Result.success(movies)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchMovies(keyword: String, page: Int): Result<List<Movie>> {
        return try {
            val response = api.searchMovies(apiKey = apiKey, keyword = keyword, page = page)
            val movies = response.items?.map { it.toDomain() } ?: emptyList()
            Result.success(movies)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMovieDetails(filmId: Int): Result<com.app.plenka.domain.model.MovieDetails> {
        return try {
            val dto = api.getFilmDetails(apiKey = apiKey, filmId = filmId)
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

