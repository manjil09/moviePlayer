package com.manjil.movieapp.domain.entities

import java.io.Serializable

data class MoviePojo(val title: String, val poster: Int, val rating: Double, val releaseYear: Int) :
    Serializable
