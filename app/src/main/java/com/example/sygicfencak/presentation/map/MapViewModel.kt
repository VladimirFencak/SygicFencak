package com.example.sygicfencak.presentation.map

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sygicfencak.domain.model.Location
import com.example.sygicfencak.domain.use_case.DeleteLocationCacheUseCase
import com.example.sygicfencak.domain.use_case.GetLocationDataUseCase
import com.example.sygicfencak.domain.use_case.StartLocationTrackingUseCase
import com.example.sygicfencak.domain.use_case.StopLocationTrackingUseCase
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val startLocationTrackingUseCase: StartLocationTrackingUseCase,
    private val stopLocationTrackingUseCase: StopLocationTrackingUseCase,
    private val getLocationDataUseCase: GetLocationDataUseCase,
    private val deleteLocationCacheUseCase: DeleteLocationCacheUseCase
) : ViewModel() {

    private val _locationData = mutableStateOf(listOf<Location>())
    val locationData: State<List<Location>> = _locationData

    private val _polylineData = mutableListOf<LatLng>()
    val polylineData: List<LatLng> = _polylineData

    private var getLocationDataJob: Job? = null

    init {
        getLocationData()
    }

    fun startLocationTracking() {
        deleteLocationCache()
        startLocationTrackingUseCase()
        getLocationData()
    }

    fun stopLocationTracking() {
        getLocationDataJob?.cancel()
        stopLocationTrackingUseCase()
    }

    private fun deleteLocationCache() {
        _locationData.value = listOf()
        _polylineData.clear()
        viewModelScope.launch {
            deleteLocationCacheUseCase()
        }
    }

    private fun getLocationData() {
        getLocationDataJob?.cancel()
        getLocationDataJob = getLocationDataUseCase()
            .onEach { locationList ->
                _locationData.value = locationList
                if (_polylineData.isEmpty()) {
                    locationList.forEach {
                        _polylineData.add(
                            LatLng(it.latitude, it.longitude)
                        )
                    }
                } else {
                    _polylineData.add(
                        LatLng(
                            locationList.last().latitude,
                            locationList.last().longitude
                        )
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}