package com.app.plenka.data.remote

import com.app.plenka.data.remote.dto.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KinopoiskApi {

    @GET("v2.2/films/collections")
    suspend fun getCollections(
        @Header("X-API-KEY") apiKey: String,
        @Query("type") type: String = "TOP_POPULAR_MOVIES",
        @Query("page") page: Int = 1
    ): MovieResponse

    @GET("v2.2/films")
    suspend fun searchMovies(
        @Header("X-API-KEY") apiKey: String,
        @Query("keyword") keyword: String,
        @Query("page") page: Int = 1
    ): MovieResponse

    companion object {
        const val BASE_URL = "https://kinopoiskapiunofficial.tech/api/"
    }
}
