package com.youngsik.jinada.presentation.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.youngsik.domain.manager.GeoFencingManager
import com.youngsik.shared.model.DataResourceResult
import com.youngsik.jinada.data.datasource.local.DataStoreDataSourceImpl
import com.youngsik.jinada.data.datasource.remote.FirestoreMemoDataSourceImpl
import com.youngsik.jinada.data.impl.CurrentLocationRepositoryImpl
import com.youngsik.jinada.data.impl.DataStoreRepositoryImpl
import com.youngsik.jinada.data.impl.MemoRepositoryImpl
import com.youngsik.jinada.data.repository.CurrentLocationRepository
import com.youngsik.jinada.data.repository.DataStoreRepository
import com.youngsik.jinada.data.repository.MemoRepository
import com.youngsik.jinada.presentation.manager.GeoFencingManagerImpl
import com.youngsik.jinada.presentation.provider.LocationRepositoryProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ActivityRecognitionService : Service(){
    companion object {
        private const val NOTIFICATION_ID = 10002
        private const val SEARCHING_RANGE = 5.0f // 5km
        const val ACTION_UPDATE_GEOFENCING = "ACTION_UPDATE_GEOFENCING"
    }

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val locationRepository: CurrentLocationRepository by lazy {
        LocationRepositoryProvider.getInstance(applicationContext)
    }

    private val memoRepository : MemoRepository by lazy {
        MemoRepositoryImpl(FirestoreMemoDataSourceImpl())
    }

    private val dataStoreRepository : DataStoreRepository by lazy {
        DataStoreRepositoryImpl(DataStoreDataSourceImpl(applicationContext))
    }

    private val geoFencingManager : GeoFencingManager by lazy {
        GeoFencingManagerImpl(applicationContext)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_UPDATE_GEOFENCING -> {
                startForegroundNotify()
                serviceScope.launch {
                    combine(dataStoreRepository.userSettings, dataStoreRepository.userInfo) { settings, userInfo ->
                        settings to userInfo
                    }.collectLatest { (settings, userInfo) ->
                        if (settings.closerNotificationEnabled){
                            val location = (locationRepository as CurrentLocationRepositoryImpl).getCurrentLocation()
                            if (location != null){
                                memoRepository.getNearByMemoList(userInfo.nickname,location,SEARCHING_RANGE).collect { result ->
                                    when (result) {
                                        is DataResourceResult.Success -> {
                                            if (ActivityCompat.checkSelfPermission(applicationContext,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                                val memoList = result.data.filter { todoItemData -> !todoItemData.isCompleted }
                                                geoFencingManager.updateGeoPencing(memoList, settings.closerMemoNotiRange)
                                            }
                                            stopForegroundNotify()
                                        }
                                        is DataResourceResult.Failure -> { stopForegroundNotify() }
                                        is DataResourceResult.Loading -> { /* 로딩 처리 */ }
                                    }
                                }
                            }
                        } else {
                            geoFencingManager.removeGeoPencing()
                        }
                    }
                    dataStoreRepository.userSettings.collect { settings ->

                    }

                }
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
        if(serviceScope.isActive) serviceScope.cancel()
        stopSelf()
    }


    override fun onDestroy() {
        super.onDestroy()
        stopForegroundNotify()
    }


}