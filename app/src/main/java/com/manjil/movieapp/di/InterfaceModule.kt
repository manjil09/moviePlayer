package com.manjil.movieapp.di

import com.manjil.movieapp.data.implementations.WeatherRepositoryImpl
import com.manjil.movieapp.domain.repo.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class InterfaceModule {
    @Binds
    abstract fun bindWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository
}