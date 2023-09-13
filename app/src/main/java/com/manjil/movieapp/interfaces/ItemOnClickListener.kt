package com.manjil.movieapp.interfaces

import com.manjil.movieapp.model.DataItem

interface ItemOnClickListener {
    fun onItemClick(
//        dataItem: MoviePojo,
        dataItem: DataItem
    )
}