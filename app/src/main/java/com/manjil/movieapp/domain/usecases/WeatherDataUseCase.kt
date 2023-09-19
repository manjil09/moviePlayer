package com.manjil.movieapp.domain.usecases

import android.util.Log
import androidx.lifecycle.LiveData
import com.manjil.movieapp.domain.entities.WeatherPojo
import com.manjil.movieapp.domain.repo.WeatherRepository
import com.manjil.movieapp.util.Result
import javax.inject.Inject

class WeatherDataUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {
    fun get(lat: Double, lon: Double): LiveData<Result<WeatherPojo>> {
        val value = weatherRepository.get(lat, lon)
        Log.d("getweather", "usecase: ${value.value}")
        return value
    }
}