package com.youngsik.jinada.data.impl

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.youngsik.domain.entity.LocationEntity
import com.youngsik.domain.repository.CurrentLocationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CurrentLocationRepositoryImpl @Inject constructor(@param:ApplicationContext private val context: Context) : CurrentLocationRepository {
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private val _latestLocation = MutableStateFlow<Location?>(null)
    override val latestLocationState: Flow<LocationEntity?> = _latestLocation.asStateFlow().map { location ->
        location?.let { LocationEntity(it.latitude, it.longitude) }
    }

    override fun updateLatestLocation(location: LocationEntity) {
        _latestLocation.value = Location("").apply {
            latitude = location.latitude
            longitude = location.longitude
        }
    }

    @SuppressLint("MissingPermission")
    override fun getCurrentLocationUpdates(): Flow<LocationEntity> = callbackFlow {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            TimeUnit.SECONDS.toMillis(5)
        ).build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    trySend(location).isSuccess
                }
            }
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

        awaitClose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }.map { location -> LocationEntity(location.latitude, location.longitude) }

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): LocationEntity? {
        val currentLocation = fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).await()
        return currentLocation?.let { LocationEntity(it.latitude, it.longitude) }
    }
}