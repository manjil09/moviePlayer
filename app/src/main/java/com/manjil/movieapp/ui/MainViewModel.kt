package com.manjil.movieapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.manjil.movieapp.domain.entities.WeatherPojo
import com.manjil.movieapp.domain.usecases.WeatherDataUseCase
import com.manjil.movieapp.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val weatherDataUseCase: WeatherDataUseCase
) : ViewModel() {

    private var _data = MutableLiveData<Result<WeatherPojo>>()
    var  data: LiveData<Result<WeatherPojo>> = _data

    private val _weatherData = MutableLiveData<WeatherPojo?>()
    val weatherData: LiveData<WeatherPojo?>
        get() = _weatherData

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getWeatherData(lat: Double, lon: Double) {
//        when (val result = weatherDataUseCase.get(lat, lon).value) {
//            is Result.Success -> _weatherData.value = result.data
//
//            is Result.Error -> _errorMessage.value = result.error
//
//            null -> {_errorMessage.value = "Could not connect to the server."
//                Log.d("getweather", "getWeatherData: null value")}
//        }
        data = weatherDataUseCase.get(lat, lon)
    }
}