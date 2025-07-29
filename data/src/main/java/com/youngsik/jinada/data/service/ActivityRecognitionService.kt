package com.youngsik.jinada.data.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.naver.maps.geometry.LatLng
import com.youngsik.jinada.data.common.LocationRepositoryProvider
import com.youngsik.jinada.data.impl.CurrentLocationRepositoryImpl
import com.youngsik.jinada.data.remote.FirestoreMemoDataSourceImpl
import com.youngsik.jinada.data.repository.CurrentLocationRepository
import com.youngsik.jinada.data.repository.MemoRepository
import com.youngsik.jinada.data.repository.MemoRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ActivityRecognitionService : Service(){
    companion object {
        private const val NOTIFICATION_ID = 12345
        const val ACTION_UPDATE_GEOFENCING = "ACTION_UPDATE_GEOFENCING"
    }

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val locationRepository: CurrentLocationRepository by lazy {
        LocationRepositoryProvider.getInstance(applicationContext)
    }

    private val memoRepository : MemoRepository by lazy {
        MemoRepositoryImpl(FirestoreMemoDataSourceImpl())
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("jinada_test", "ActivityRecognitionService onStartCommand")
        when (intent?.action) {
            ACTION_UPDATE_GEOFENCING -> {
                Log.d("jinada_test", "ActivityRecognitionService ACTION_UPDATE_GEOFENCING")
                startForegroundNotify()
                locationRepository.getCurrentLocationUpdates()
                    .onEach { location ->
                        (locationRepository as CurrentLocationRepositoryImpl).updateLatestLocation(location)
                        // TODO: 해당 수신 위치 기준으로 가장 인접한 메모 100개 가져와서 지오펜싱 등록하기

                    }
                    .catch { e -> e.printStackTrace() }
                    .launchIn(serviceScope)
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private lateinit var notificationManager: NotificationManager

    @SuppressLint("ForegroundServiceType")
    private fun startForegroundNotify() {
        val channelId = "location_notification_channel"
        notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "Activity Recognition Service",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("지나다")
            .setContentText("인접한 위치의 메모 목록으로 알람을 업데이트 합니다..")
            .setOngoing(true)
            .build()
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun stopForegroundNotify(){
        notificationManager.cancel(NOTIFICATION_ID)
        stopSelf()
    }


    override fun onDestroy() {
        super.onDestroy()
        stopForegroundNotify()
        serviceScope.cancel()
    }


}