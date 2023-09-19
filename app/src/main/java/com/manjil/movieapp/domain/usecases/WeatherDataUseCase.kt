package com.manjil.movieapp.domain.usecases

import androidx.lifecycle.LiveData
import com.manjil.movieapp.domain.entities.WeatherPojo
import com.manjil.movieapp.domain.repo.WeatherRepository
import com.manjil.movieapp.util.Result

class WeatherDataUseCase(private val weatherRepository: WeatherRepository) {
    fun get(lat: Double, lon: Double): LiveData<Result<WeatherPojo>> {
        return weatherRepository.get(lat, lon)
    }
}