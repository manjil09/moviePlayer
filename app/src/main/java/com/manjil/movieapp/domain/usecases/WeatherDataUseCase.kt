package com.manjil.movieapp.domain.usecases

import com.manjil.movieapp.domain.entities.WeatherPojo
import com.manjil.movieapp.domain.repo.WeatherRepository
import com.manjil.movieapp.util.Result
import javax.inject.Inject

class WeatherDataUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {
    suspend fun get(lat: Double, lon: Double): Result<WeatherPojo> {
        return weatherRepository.get(lat, lon)
    }
}