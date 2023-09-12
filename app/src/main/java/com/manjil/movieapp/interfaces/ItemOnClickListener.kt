package com.manjil.movieapp.interfaces

import com.manjil.movieapp.model.DataItem
import com.manjil.movieapp.model.MoviePojo
import com.manjil.movieapp.model.WeatherPojo

interface ItemOnClickListener {
    fun onItemClick(
//        dataItem: MoviePojo,
        dataItem: DataItem
    )
}