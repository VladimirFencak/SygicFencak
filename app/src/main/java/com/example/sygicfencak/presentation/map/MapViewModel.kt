package com.example.sygicfencak.presentation.map

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.sygicfencak.domain.use_case.StartLocationTrackingUseCase
import com.example.sygicfencak.domain.use_case.StopLocationTrackingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val startLocationTrackingUseCase: StartLocationTrackingUseCase,
    private val stopLocationTrackingUseCase: StopLocationTrackingUseCase
): ViewModel(){

    fun startLocationTracking(){
        startLocationTrackingUseCase()
    }

    fun stopLocationTracking(){
        Log.e("logujeme","view model vypina")
        stopLocationTrackingUseCase()
    }
}