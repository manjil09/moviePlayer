package com.manjil.movieapp.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiInstance {
    //    private const val BASE_URL = "https://weatherbit-v1-mashape.p.rapidapi.com/"
    private const val BASE_URL = "http://10.0.2.2/manjilKoju/"
    private var retrofit: Retrofit? = null

    fun getInstance(): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return retrofit ?: Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build().also {
                retrofit = it
            }
    }
}