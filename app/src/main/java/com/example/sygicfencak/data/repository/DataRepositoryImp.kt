package com.example.sygicfencak.data.repository

import android.content.Context
import android.util.Log
import com.example.sygicfencak.data.location_service.runLocationService
import com.example.sygicfencak.domain.model.Location
import com.example.sygicfencak.domain.repository.DataRepository
import kotlinx.coroutines.flow.Flow

class DataRepositoryImp(
    private val context: Context
) : DataRepository {
    override fun getLocations(): Flow<List<Location>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertLocation(location: Location) {
        TODO("Not yet implemented")
    }

    override fun startLocationTracking() {
        Log.e("logujeme","Infrastructure imp zapina")
        runLocationService(true, context)
    }

    override fun stopLocationTracking() {
        Log.e("logujeme","Infrastructure imp vypina")
        runLocationService(false, context)
    }

}