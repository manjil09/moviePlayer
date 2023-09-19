package com.manjil.movieapp.domain.repo

import androidx.lifecycle.LiveData
import com.manjil.movieapp.domain.entities.WeatherPojo
import com.manjil.movieapp.util.Result

interface WeatherRepository {
    fun get(lat:Double, lon:Double): LiveData<Result<WeatherPojo>>
}