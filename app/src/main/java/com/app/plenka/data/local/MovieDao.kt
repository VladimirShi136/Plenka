package com.app.plenka.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.plenka.data.local.entity.MovieEntity
import com.app.plenka.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies_cache WHERE category = :category")
    suspend fun getCachedMovies(category: String): List<MovieEntity>

    @Query("SELECT * FROM movies_cache WHERE category = :category AND page = :page")
    suspend fun getCachedMoviesByPage(category: String, page: Int): List<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoviesCache(movies: List<MovieEntity>)

    @Query("DELETE FROM movies_cache")
    suspend fun clearAllCache()

    @Query("SELECT * FROM favorite_movies")
    fun getFavoriteMovies(): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM favorite_movies WHERE id = :movieId")
    suspend fun getMovieById(movieId: Int): FavoriteEntity?

    @Query("SELECT * FROM favorite_movies WHERE id = :movieId")
    fun observeMovieById(movieId: Int): Flow<FavoriteEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(movie: FavoriteEntity)

    @Query("DELETE FROM favorite_movies WHERE id = :movieId")
    suspend fun removeFavorite(movieId: Int)
}
