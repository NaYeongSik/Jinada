package com.youngsik.jinada.data.repository

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface CurrentLocationRepository {
    fun getCurrentLocationUpdates(): Flow<Location>
    suspend fun getCurrentLocation(): Location?
}