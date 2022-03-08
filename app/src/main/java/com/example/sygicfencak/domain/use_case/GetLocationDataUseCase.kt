package com.example.sygicfencak.domain.use_case

import com.example.sygicfencak.domain.model.Location
import com.example.sygicfencak.domain.repository.DataRepository
import kotlinx.coroutines.flow.Flow

class GetLocationDataUseCase(
    private val dataRepository: DataRepository
) {
    operator fun invoke():Flow<List<Location>>{
        return dataRepository.getLocations()
    }
}