package com.manjil.movieapp.ui.interfaces

import com.manjil.movieapp.domain.entities.DataItem

interface ItemOnClickListener {
    fun onItemClick(dataItemList: List<DataItem?>?, position: Int)
}