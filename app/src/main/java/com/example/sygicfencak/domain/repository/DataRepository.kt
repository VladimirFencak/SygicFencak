package com.example.sygicfencak.domain.repository

import com.example.sygicfencak.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface DataRepository {
    fun getLocations(): Flow<List<Location>>

    suspend fun insertLocation(location: Location)

    suspend fun deleteLocationCache()

    fun startLocationTracking()

    fun stopLocationTracking()
}