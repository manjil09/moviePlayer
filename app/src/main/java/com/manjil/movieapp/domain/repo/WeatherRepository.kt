package com.manjil.movieapp.domain.repo

import androidx.lifecycle.LiveData
import com.manjil.movieapp.R
import com.manjil.movieapp.domain.entities.MoviePojo
import com.manjil.movieapp.domain.entities.WeatherPojo
import com.manjil.movieapp.util.Result

interface WeatherRepository {
    suspend fun get(lat:Double, lon:Double): Result<WeatherPojo>
//    fun getMovieList(): ArrayList<MoviePojo> {
//        val moviePojoList = ArrayList<MoviePojo>()
//        moviePojoList.add(MoviePojo("First Title", R.mipmap.image, 7.7, 2023))
//        moviePojoList.add(MoviePojo("Second Title", R.mipmap.image, 8.6, 2018))
//        moviePojoList.add(MoviePojo("Third Title", R.mipmap.image, 5.3, 2003))
//        return moviePojoList
//    }
}