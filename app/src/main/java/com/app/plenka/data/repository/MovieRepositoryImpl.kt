package com.app.plenka.data.repository

import com.app.plenka.BuildConfig
import com.app.plenka.data.mapper.toDomain
import com.app.plenka.data.remote.KinopoiskApi
import com.app.plenka.domain.model.Movie
import com.app.plenka.domain.repository.MovieRepository
import com.app.plenka.data.local.MovieDao
import com.app.plenka.data.local.entity.toDomain
import com.app.plenka.data.local.entity.toEntity
import com.app.plenka.data.local.entity.toFavoriteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

/**
 * Реализация интерфейса репозитория
 */
class MovieRepositoryImpl @Inject constructor(
    private val api: KinopoiskApi,
    private val movieDao: MovieDao
) : MovieRepository {

    private val apiKey: String
        get() = BuildConfig.KP_API_KEY

    private val favoriteMutex = Mutex()

    override suspend fun getPopularMovies(page: Int): Result<List<Movie>> {
        val category = "popular"
        return try {
            val response = api.getCollections(apiKey = apiKey, type = "TOP_POPULAR_MOVIES", page = page)
            val movies = response.items?.map { it.toDomain() } ?: emptyList()
            // Сохраняем в кэш
            val entities = movies.map { it.toEntity(category, page) }
            movieDao.insertMoviesCache(entities)
            Result.success(movies)
        } catch (e: kotlin.coroutines.cancellation.CancellationException) {
            throw e
        } catch (e: Exception) {
            // При ошибке сети пробуем вернуть из локального кэша (все страницы категории)
            val cached = movieDao.getCachedMovies(category)
            if (cached.isNotEmpty()) {
                Result.success(cached.sortedBy { it.page }.map { it.toDomain() })
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun searchMovies(keyword: String, page: Int): Result<List<Movie>> {
        val category = "search_${keyword.trim().lowercase()}"
        return try {
            val response = api.searchMovies(apiKey = apiKey, keyword = keyword, page = page)
            val movies = response.items?.map { it.toDomain() } ?: emptyList()
            val entities = movies.map { it.toEntity(category, page) }
            movieDao.insertMoviesCache(entities)
            Result.success(movies)
        } catch (e: kotlin.coroutines.cancellation.CancellationException) {
            throw e
        } catch (e: Exception) {
            val cached = movieDao.getCachedMoviesByPage(category, page)
            val allCached = if (page == 1) movieDao.getCachedMovies(category) else cached
            if (allCached.isNotEmpty()) {
                Result.success(allCached.sortedBy { it.page }.map { it.toDomain() })
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun getMovieDetails(filmId: Int): Result<com.app.plenka.domain.model.MovieDetails> {
        return try {
            val dto = api.getFilmDetails(apiKey = apiKey, filmId = filmId)
            val videosResponse = try {
                api.getFilmVideos(apiKey = apiKey, filmId = filmId)
            } catch (_: Exception) {
                null
            }
            val kpWidget = videosResponse?.items?.firstOrNull { 
                it.site.equals("KINOPOISK_WIDGET", ignoreCase = true) && !it.url.isNullOrBlank()
            }?.url

            val youtubeVideo = videosResponse?.items?.firstOrNull { 
                it.site.equals("YOUTUBE", ignoreCase = true) && !it.url.isNullOrBlank()
            }?.url

            Result.success(dto.toDomain(trailerUrl = kpWidget, youtubeTrailerUrl = youtubeVideo))
        } catch (e: kotlin.coroutines.cancellation.CancellationException) {
            throw e
        } catch (e: Exception) {
            // Если нет сети, пробуем взять базовые данные из избранного
            val favorite = movieDao.getMovieById(filmId)
            if (favorite != null) {
                val details = com.app.plenka.domain.model.MovieDetails(
                    id = favorite.id,
                    title = favorite.title,
                    originalTitle = favorite.originalTitle,
                    year = favorite.year,
                    posterUrl = favorite.posterUrl,
                    rating = favorite.rating,
                    ratingImdb = null,
                    genres = favorite.genres,
                    description = null,
                    shortDescription = null,
                    filmLength = null,
                    countries = emptyList(),
                    trailerUrl = null,
                    youtubeTrailerUrl = null
                )
                Result.success(details)
            } else {
                Result.failure(e)
            }
        }
    }

    override fun getFavoriteMovies(): Flow<List<Movie>> {
        return movieDao.getFavoriteMovies().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeIsFavorite(movieId: Int): Flow<Boolean> {
        return movieDao.observeMovieById(movieId).map { it != null }
    }

    override suspend fun isFavorite(movieId: Int): Boolean {
        return movieDao.getMovieById(movieId) != null
    }

    override suspend fun toggleFavorite(movie: Movie) {
        favoriteMutex.withLock {
            val existing = movieDao.getMovieById(movie.id)
            if (existing != null) {
                movieDao.removeFavorite(movie.id)
            } else {
                movieDao.insertFavorite(movie.toFavoriteEntity())
            }
        }
    }
}
