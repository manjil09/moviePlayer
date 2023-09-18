package com.manjil.movieapp.api

import com.manjil.movieapp.domain.entities.WeatherPojo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {
    @Headers(
        "X-RapidAPI-Key: 4433b8c9bcmsh567c2114d821c93p1b8766jsncd5d94c028ff",
        "X-RapidAPI-Host: weatherbit-v1-mashape.p.rapidapi.com"
    )
//    @GET("forecast/daily")
    @GET("dailyWeather.json")
    fun getWeatherData(@Query("lat") lat: Double, @Query("lon") lon: Double): Call<WeatherPojo>
}