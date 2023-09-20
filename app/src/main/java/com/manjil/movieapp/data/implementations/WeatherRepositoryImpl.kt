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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : WeatherRepository {
    override suspend fun get(lat: Double, lon: Double): Result<WeatherPojo> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getWeatherData(lat, lon)
                if (response.isSuccessful) {
                    val weatherData = response.body()
                    if (weatherData != null)
                        Result.Success(weatherData)
                    else
                        Result.Error("No data available.")
                } else
                    Result.Error("Could not fetch Weather Data.")
            } catch (e: Exception) {
                Log.d("getweather", "get: ${e.message}")
                Result.Error("Could not connect to the server.")
            }
        }
    }
}