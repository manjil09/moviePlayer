package com.manjil.movieapp.data.services

import com.manjil.movieapp.data.constants.AppConstants
import com.manjil.movieapp.domain.entities.WeatherPojo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {
    @Headers(AppConstants.RAPID_API_KEY, AppConstants.RAPID_API_HOST)
//    @GET("forecast/daily")
    @GET("dailyWeather.json")
    suspend fun getWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Response<WeatherPojo>
}