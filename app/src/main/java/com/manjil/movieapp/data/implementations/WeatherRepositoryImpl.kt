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
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : WeatherRepository {
    override suspend fun get(lat: Double, lon: Double): Result<WeatherPojo> {
        return suspendCoroutine {
            apiService.getWeatherData(lat, lon).enqueue(object :
                Callback<WeatherPojo> {
                override fun onResponse(call: Call<WeatherPojo>, response: Response<WeatherPojo>) {
                    if (response.isSuccessful) {
                        val weatherPojo = response.body()

                        if (weatherPojo != null)
                            it.resume(Result.Success(weatherPojo))
                        else
                            it.resume(Result.Error("No data available."))
                        Log.d("getWeather", "onResponse: ${response.code()} ${response.message()}")
                    } else {
                        Log.d("getWeather", "onResponse: ${response.code()} ${response.message()}")
                        it.resume(Result.Error("Could not fetch Weather Data."))
                    }
                }

                override fun onFailure(call: Call<WeatherPojo>, t: Throwable) {
                    Log.d("getWeather", "onFailure: couldn't connect to server")
                    t.printStackTrace()
                    it.resume(Result.Error("Could not connect to the server."))
                }
            })
        }
    }
}