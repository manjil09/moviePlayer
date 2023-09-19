package com.manjil.movieapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.manjil.movieapp.domain.entities.WeatherPojo
import com.manjil.movieapp.domain.usecases.WeatherDataUseCase
import com.manjil.movieapp.util.Result

class MainViewModel(private val weatherDataUseCase: WeatherDataUseCase) : ViewModel() {

    private val _weatherData = MutableLiveData<WeatherPojo?>()
    val weatherData: LiveData<WeatherPojo?>
        get() = _weatherData

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getWeatherData(lat: Double, lon: Double) {
        val result = weatherDataUseCase.get(lat, lon).value
        when (result) {
            is Result.Success -> _weatherData.value = result.data

            is Result.Error -> _errorMessage.value = result.error

            null -> _errorMessage.value = "Could not connect to the server."
        }
    }
}