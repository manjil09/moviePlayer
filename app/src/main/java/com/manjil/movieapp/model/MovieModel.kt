package com.manjil.movieapp.model

import com.manjil.movieapp.R
import com.manjil.movieapp.api.ApiInstance
import com.manjil.movieapp.api.ApiService
import com.manjil.movieapp.domain.entities.MoviePojo
import com.manjil.movieapp.domain.entities.WeatherPojo
import retrofit2.Call

class MovieModel {
    fun getMovieList(): ArrayList<MoviePojo> {
        val moviePojoList = ArrayList<MoviePojo>()
        moviePojoList.add(MoviePojo("First Title", R.mipmap.image, 7.7, 2023))
        moviePojoList.add(MoviePojo("Second Title", R.mipmap.image, 8.6, 2018))
        moviePojoList.add(MoviePojo("Third Title", R.mipmap.image, 5.3, 2003))
        return moviePojoList
    }

    fun getWeatherData(lat: Double, lon: Double): Call<WeatherPojo> {
        val retrofit = ApiInstance.getInstance()
        return retrofit.create(ApiService::class.java).getWeatherData(lat, lon)
    }
}