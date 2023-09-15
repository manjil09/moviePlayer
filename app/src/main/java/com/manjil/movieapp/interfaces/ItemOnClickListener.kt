package com.manjil.movieapp.interfaces

import com.manjil.movieapp.model.DataItem

interface ItemOnClickListener {
    fun onItemClick(dataItemList: List<DataItem?>?, position: Int)
}