package com.rng350.mediatracker.common.di

import android.content.Context
import coil3.ImageLoader
import com.rng350.mediatracker.common.SaveImageFromURLUseCase
import com.rng350.mediatracker.common.database.MovieDao
import com.rng350.mediatracker.common.database.MovieGenreDao
import com.rng350.mediatracker.common.database.MovieStaffDao
import com.rng350.mediatracker.movies.usecases.SaveMovieDetailsToDatabaseUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @Provides
    @ViewModelScoped
    fun provideSaveMovieDetailsToDatabaseUseCase(
        saveImageFromURLUseCase: SaveImageFromURLUseCase,
        movieDao: MovieDao,
        movieStaffDao: MovieStaffDao,
        movieGenreDao: MovieGenreDao
    ) = SaveMovieDetailsToDatabaseUseCase(
        saveImageFromUrl = saveImageFromURLUseCase,
        movieDao = movieDao,
        movieStaffDao = movieStaffDao,
        movieGenreDao = movieGenreDao
    )

    @Provides
    @ViewModelScoped
    fun provideSaveImageFromUrlUseCase(
        @ApplicationContext context: Context,
        imageLoader: ImageLoader
    ) = SaveImageFromURLUseCase(
        context,
        imageLoader
    )
}