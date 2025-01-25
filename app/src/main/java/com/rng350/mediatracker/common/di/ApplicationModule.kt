package com.rng350.mediatracker.common.di

import android.app.Application
import android.content.Context
import android.provider.MediaStore.Audio.Media
import androidx.room.Room
import coil3.ImageLoader
import com.rng350.mediatracker.BuildConfig
import com.rng350.mediatracker.common.Constants
import com.rng350.mediatracker.common.database.MediaTrackerDatabase
import com.rng350.mediatracker.common.database.MovieDao
import com.rng350.mediatracker.common.database.MovieGenreDao
import com.rng350.mediatracker.common.database.MovieStaffDao
import com.rng350.mediatracker.networking.TMDBApi
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
            addInterceptor { chain ->
                val originalRequest = chain.request()
                val urlWithApiKey = originalRequest.url.newBuilder()
                    .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                    .build()
                val newRequest = originalRequest.newBuilder()
                    .url(urlWithApiKey)
                    .build()
                chain.proceed(newRequest)
            }
            build()
        }
        return Retrofit.Builder()
            .baseUrl(Constants.TMDB_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
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
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun movieDao(database: MediaTrackerDatabase): MovieDao {
        return database.movieDao
    }

    @Provides
    fun movieStaffDao(database: MediaTrackerDatabase): MovieStaffDao {
        return database.movieStaffDao
    }

    @Provides
    fun movieGenreDao(database: MediaTrackerDatabase): MovieGenreDao {
        return database.movieGenreDao
    }

    @Provides
    fun theMovieDbApi(retrofit: Retrofit): TMDBApi {
        return retrofit.create(TMDBApi::class.java)
    }

    @Provides
    fun imageLoader(@ApplicationContext context: Context): ImageLoader {
        return ImageLoader.Builder(context).build()
    }
}