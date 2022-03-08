package com.example.sygicfencak.presentation.map

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sygicfencak.domain.model.Location
import com.example.sygicfencak.domain.use_case.GetLocationDataUseCase
import com.example.sygicfencak.domain.use_case.StartLocationTrackingUseCase
import com.example.sygicfencak.domain.use_case.StopLocationTrackingUseCase
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val startLocationTrackingUseCase: StartLocationTrackingUseCase,
    private val stopLocationTrackingUseCase: StopLocationTrackingUseCase,
    private val getLocationDataUseCase: GetLocationDataUseCase
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
        startLocationTrackingUseCase()
        getLocationData()
    }

    fun stopLocationTracking() {
        Log.e("logujeme", "view model vypina")
        getLocationDataJob?.cancel()
        stopLocationTrackingUseCase()
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