package com.app.plenka.di

import com.app.plenka.data.remote.KinopoiskApi
import com.app.plenka.data.repository.MovieRepositoryImpl
import com.app.plenka.domain.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideKinopoiskApi(okHttpClient: OkHttpClient): KinopoiskApi {
        return Retrofit.Builder()
            .baseUrl(KinopoiskApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KinopoiskApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieRepository(api: KinopoiskApi): MovieRepository {
        return MovieRepositoryImpl(api)
    }
}
