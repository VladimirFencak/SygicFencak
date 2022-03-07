package com.example.sygicfencak.domain.use_case

import android.util.Log
import com.example.sygicfencak.domain.repository.DataRepository

class StartLocationTrackingUseCase(
    private val dataRepository: DataRepository
) {
    operator fun invoke(){
        Log.e("logujeme","UseCase invoke")
        dataRepository.startLocationTracking()
    }
}