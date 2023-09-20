package com.manjil.movieapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manjil.movieapp.domain.entities.WeatherPojo
import com.manjil.movieapp.domain.usecases.WeatherDataUseCase
import com.manjil.movieapp.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class  MainViewModel @Inject constructor(
    private val weatherDataUseCase: WeatherDataUseCase
) : ViewModel() {

    private val _weatherData = MutableLiveData<WeatherPojo>()
    val weatherData: LiveData<WeatherPojo>
        get() = _weatherData

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getWeatherData(lat: Double, lon: Double) {
        viewModelScope.launch{
            when (val weatherData = weatherDataUseCase.get(lat, lon)) {
                is Result.Success -> _weatherData.value = weatherData.data
                is Result.Error -> _errorMessage.value = weatherData.error
            }
        }
    }
}