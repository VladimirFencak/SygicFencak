package com.example.sygicfencak.di

import android.content.Context
import androidx.room.Room
import com.example.sygicfencak.data.location_data.LocationDatabase
import com.example.sygicfencak.data.repository.DataRepositoryImp
import com.example.sygicfencak.domain.use_case.StartLocationTrackingUseCase
import com.example.sygicfencak.domain.use_case.StopLocationTrackingUseCase
import com.example.sygicfencak.domain.repository.DataRepository
import com.example.sygicfencak.domain.use_case.DeleteLocationCacheUseCase
import com.example.sygicfencak.domain.use_case.GetLocationDataUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideLocationDatabase(@ApplicationContext appContext: Context): LocationDatabase {
        return Room.databaseBuilder(
            appContext,
            LocationDatabase::class.java,
            "location_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideDataRepository(
        @ApplicationContext appContext: Context,
        database: LocationDatabase
    ): DataRepository {
        return DataRepositoryImp(appContext, database.locationDao)
    }

    @Provides
    @Singleton
    fun provideStartLocationTrackingUseCase(dataRepository: DataRepository): StartLocationTrackingUseCase {
        return StartLocationTrackingUseCase(dataRepository)
    }

    @Provides
    @Singleton
    fun provideStopLocationTrackingUseCase(dataRepository: DataRepository): StopLocationTrackingUseCase {
        return StopLocationTrackingUseCase(dataRepository)
    }

    @Provides
    @Singleton
    fun provideGetLocationDataUseCase(dataRepository: DataRepository): GetLocationDataUseCase {
        return GetLocationDataUseCase(dataRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteLocationCacheUseCase(dataRepository: DataRepository): DeleteLocationCacheUseCase {
        return DeleteLocationCacheUseCase(dataRepository)
    }
}