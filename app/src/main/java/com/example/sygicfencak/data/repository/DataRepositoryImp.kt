package com.example.sygicfencak.data.repository

import android.content.Context
import com.example.sygicfencak.data.location_data.LocationDao
import com.example.sygicfencak.data.location_service.runLocationService
import com.example.sygicfencak.domain.model.Location
import com.example.sygicfencak.domain.repository.DataRepository
import kotlinx.coroutines.flow.Flow

class DataRepositoryImp(
    private val context: Context,
    private val dao: LocationDao
) : DataRepository {
    override fun getLocations(): Flow<List<Location>> {
        return dao.getLocation()
    }

    override suspend fun insertLocation(location: Location) {
        dao.insertLocation(location)
    }

    override suspend fun deleteLocationCache() {
        dao.deleteLocationCache()
    }

    override fun startLocationTracking() {
        runLocationService(true, context)
    }

    override fun stopLocationTracking() {
        runLocationService(false, context)
    }

}