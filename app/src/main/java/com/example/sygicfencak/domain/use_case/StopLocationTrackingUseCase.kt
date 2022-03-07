package com.example.sygicfencak.domain.use_case

import android.util.Log
import com.example.sygicfencak.domain.repository.DataRepository

class StopLocationTrackingUseCase(
    private val dataRepository: DataRepository
) {
    operator fun invoke(){
        Log.e("logujeme","usecase vypina")
        dataRepository.stopLocationTracking()
    }
}