package com.manjil.movieapp.data.implementations

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.manjil.movieapp.data.services.ApiService
import com.manjil.movieapp.domain.entities.WeatherPojo
import com.manjil.movieapp.domain.repo.WeatherRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.manjil.movieapp.util.Result

class WeatherRepositoryImpl(private val apiService: ApiService) : WeatherRepository {
    override fun get(lat: Double, lon: Double): LiveData<Result<WeatherPojo>> {
        val liveData = MutableLiveData<Result<WeatherPojo>>()

        apiService.getWeatherData(lat, lon).enqueue(object :
            Callback<WeatherPojo> {
            override fun onResponse(call: Call<WeatherPojo>, response: Response<WeatherPojo>) {
                if (response.isSuccessful) {
                    val weatherPojo = response.body()

                    if (weatherPojo != null)
                        liveData.value = Result.Success(weatherPojo)
                    else
                        liveData.value = Result.Error("No data available.")

                    Log.d("getWeather", "onResponse: ${response.code()} ${response.message()}")
                } else {
                    Log.d("getWeather", "onResponse: ${response.code()} ${response.message()}")
                    liveData.value = Result.Error("Could not fetch Weather Data.")
                }
            }

            override fun onFailure(call: Call<WeatherPojo>, t: Throwable) {
                Log.d("getWeather", "onFailure: couldn't connect to server")
                t.printStackTrace()
                liveData.value = Result.Error("Could not connect to the server.")
            }
        })
        return liveData
    }
}