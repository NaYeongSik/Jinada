package com.youngsik.jinada.presentation.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.youngsik.jinada.data.impl.CurrentLocationRepositoryImpl
import com.youngsik.jinada.data.repository.CurrentLocationRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import javax.inject.Inject


@AndroidEntryPoint
class CurrentLocationService : Service() {

    companion object {
        const val NOTIFICATION_ID = 10003
    }
    @Inject lateinit var locationRepository: CurrentLocationRepository

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForegroundNotify()

        locationRepository.getCurrentLocationUpdates()
            .onEach { location ->
                (locationRepository as CurrentLocationRepositoryImpl).updateLatestLocation(location)
            }
            .catch { e -> e.printStackTrace() }
            .launchIn(serviceScope)

        return START_STICKY
    }

    private lateinit var notificationManager: NotificationManager

    @SuppressLint("ForegroundServiceType")
    private fun startForegroundNotify() {
        val channelId = "location_notification_channel"
        notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "Current Location Service",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("지나다")
            .setContentText("인접한 메모가 있는지 확인중 입니다..")
            .setOngoing(true)
            .build()
        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
        if (serviceScope.isActive) serviceScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}