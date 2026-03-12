package com.youngsik.jinada.data.di

import com.youngsik.domain.repository.CurrentLocationRepository
import com.youngsik.jinada.data.impl.CurrentLocationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {
    @Binds
    @Singleton
    abstract fun bindCurrentLocationRepository(currentLocationRepository: CurrentLocationRepositoryImpl): CurrentLocationRepository
}