package com.youngsik.jinada.presentation.di

import com.youngsik.domain.manager.ActivityRecognitionManager
import com.youngsik.domain.manager.GeoFencingManager
import com.youngsik.domain.manager.LocationServiceManager
import com.youngsik.jinada.presentation.manager.ActivityRecognitionManagerImpl
import com.youngsik.jinada.presentation.manager.GeoFencingManagerImpl
import com.youngsik.jinada.presentation.manager.LocationServiceManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ManagerModule {
    @Binds
    @Singleton
    abstract fun bindLocationServiceManager(locationServiceManager: LocationServiceManagerImpl): LocationServiceManager

    @Binds
    @Singleton
    abstract fun bindGeoFencingManager(geoFencingManagerImpl: GeoFencingManagerImpl): GeoFencingManager

    @Binds
    @Singleton
    abstract fun bindActivityRecognitionManager(activityRecognitionManagerImpl: ActivityRecognitionManagerImpl): ActivityRecognitionManager

}