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

import android.content.Context
import androidx.room.Room
import com.app.plenka.data.local.MovieDao
import com.app.plenka.data.local.PlenkaDatabase
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                // BODY пишет в Logcat и заголовки, в том числе X-API-KEY. Только BASIC и только в debug
                level = if (com.app.plenka.BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BASIC
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
                redactHeader("X-API-KEY")
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
    fun providePlenkaDatabase(@ApplicationContext context: Context): PlenkaDatabase {
        return Room.databaseBuilder(
            context,
            PlenkaDatabase::class.java,
            "plenka_database"
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieDao(database: PlenkaDatabase): MovieDao {
        return database.movieDao()
    }

    @Provides
    @Singleton
    fun provideMovieRepository(api: KinopoiskApi, movieDao: MovieDao): MovieRepository {
        return MovieRepositoryImpl(api, movieDao)
    }
}
