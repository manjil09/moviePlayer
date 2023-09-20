package com.manjil.movieapp.data.implementations

import android.util.Log
import com.manjil.movieapp.data.services.ApiService
import com.manjil.movieapp.di.IoDispatcher
import com.manjil.movieapp.domain.entities.WeatherPojo
import com.manjil.movieapp.domain.repo.WeatherRepository
import com.manjil.movieapp.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : WeatherRepository {
    override suspend fun get(lat: Double, lon: Double): Result<WeatherPojo> =
        withContext(ioDispatcher) {
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