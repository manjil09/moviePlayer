package com.manjil.movieapp.interfaces

import com.manjil.movieapp.domain.entities.DataItem

interface ItemOnClickListener {
    fun onItemClick(dataItemList: List<DataItem?>?, position: Int)
}