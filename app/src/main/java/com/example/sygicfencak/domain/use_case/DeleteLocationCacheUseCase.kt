package com.example.sygicfencak.domain.use_case

import com.example.sygicfencak.domain.repository.DataRepository

class DeleteLocationCacheUseCase(
    private val dataRepository: DataRepository
) {
    suspend operator fun invoke() {
        dataRepository.deleteLocationCache()
    }
}