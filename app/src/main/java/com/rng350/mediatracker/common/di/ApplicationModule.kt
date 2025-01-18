package com.rng350.mediatracker.common.di

import android.app.Application
import androidx.room.Room
import com.rng350.mediatracker.BuildConfig
import com.rng350.mediatracker.common.Constants
import com.rng350.mediatracker.common.database.MediaTrackerDatabase
import com.rng350.mediatracker.common.database.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    fun retrofit(): Retrofit {
        val httpClient = OkHttpClient.Builder().run {
            addInterceptor(HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG) {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            })
            build()
        }
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(httpClient)
            .build()
    }

    @Provides
    @Singleton
    fun mediaTrackerDatabase(application: Application): MediaTrackerDatabase {
        return Room.databaseBuilder(
            application,
            MediaTrackerDatabase::class.java,
            Constants.DB_NAME
        ).build()
    }

    @Provides
    fun movieDao(database: MediaTrackerDatabase): MovieDao {
        return database.movieDao
    }
}