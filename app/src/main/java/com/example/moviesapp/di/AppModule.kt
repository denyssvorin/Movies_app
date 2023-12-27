package com.example.moviesapp.di

import android.app.Application
import androidx.room.Room
import com.example.moviesapp.data.MovieDao
import com.example.moviesapp.data.MovieDatabase
import com.example.moviesapp.repo.MoviesLoadRepository
import com.example.moviesapp.repo.ZipExtractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())

    @Provides
    fun provideRepo(dao: MovieDao): MoviesLoadRepository {
        return MoviesLoadRepository(dao)
    }

    @Provides
    fun provideZipExtractor(context: Application): ZipExtractor {
        return ZipExtractor(context)
    }

    @Provides
    @Singleton
    fun provideDatabase(application: Application) = Room.databaseBuilder(
        application, MovieDatabase::class.java, "movie.db"
    )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun provideMovieDao(db: MovieDatabase) = db.dao()

}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope