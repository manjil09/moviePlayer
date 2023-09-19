package com.manjil.movieapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manjil.movieapp.model.MovieModel
import com.manjil.movieapp.domain.entities.MoviePojo
import com.manjil.movieapp.domain.entities.WeatherPojo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BaseViewModel : ViewModel() {
    private val movieModel = MovieModel()

    private val _weatherData = MutableLiveData<WeatherPojo>()
    val weatherData: LiveData<WeatherPojo>
        get() = _weatherData

    private val _movieList = MutableLiveData<ArrayList<MoviePojo>>()
    val movieList: LiveData<ArrayList<MoviePojo>>
        get() = _movieList

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getWeatherData(lat: Double, lon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            movieModel.getWeatherData(lat, lon).enqueue(object : Callback<WeatherPojo> {
                override fun onResponse(call: Call<WeatherPojo>, response: Response<WeatherPojo>) {
                    if (response.isSuccessful) {
                        _weatherData.value = response.body()
                        Log.d("getWeather", "onResponse: ${response.code()} ${response.message()}")
                    } else {
                        Log.d("getWeather", "onResponse: ${response.code()} ${response.message()}")
                        _errorMessage.value = "Could not fetch Weather Data."
                    }
                }

                override fun onFailure(call: Call<WeatherPojo>, t: Throwable) {
                    Log.d("getWeather", "onFailure: couldn't connect to server")
                    t.printStackTrace()
                    _errorMessage.value = "Could not connect to the server."
                }
            })
        }
    }
}