package com.youngsik.jinada.presentation.manager

import android.content.Context
import android.content.Intent
import com.youngsik.domain.manager.LocationServiceManager
import com.youngsik.jinada.presentation.service.CurrentLocationService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocationServiceManagerImpl @Inject constructor(@param:ApplicationContext private val context: Context): LocationServiceManager {
    override fun startLocationTracking() {
        context.startForegroundService(Intent(context, CurrentLocationService::class.java))
    }

    override fun stopLocationTracking() {
        context.stopService(Intent(context, CurrentLocationService::class.java))
    }
}