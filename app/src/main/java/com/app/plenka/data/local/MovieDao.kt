package com.app.plenka.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.plenka.data.local.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies_cache WHERE category = :category AND page = :page")
    suspend fun getCachedMovies(category: String, page: Int): List<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoviesCache(movies: List<MovieEntity>)

    @Query("DELETE FROM movies_cache")
    suspend fun clearAllCache()

    @Query("SELECT * FROM favorite_movies")
    fun getFavoriteMovies(): Flow<List<com.app.plenka.data.local.entity.FavoriteEntity>>

    @Query("SELECT * FROM favorite_movies WHERE id = :movieId")
    suspend fun getMovieById(movieId: Int): com.app.plenka.data.local.entity.FavoriteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(movie: com.app.plenka.data.local.entity.FavoriteEntity)

    @Query("DELETE FROM favorite_movies WHERE id = :movieId")
    suspend fun removeFavorite(movieId: Int)
}
