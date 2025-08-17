package com.youngsik.jinada.presentation.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.youngsik.shared.model.DataResourceResult
import com.youngsik.domain.entity.TodoItemData
import com.youngsik.jinada.data.datasource.remote.FirestoreMemoDataSourceImpl
import com.youngsik.jinada.data.impl.MemoRepositoryImpl
import com.youngsik.jinada.data.repository.MemoRepository
import com.youngsik.shared.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class GeoFensingNotificationService: Service() {
    companion object{
        private const val NOTIFICATION_ID = 10001
    }

    override fun onBind(p0: Intent?): IBinder? = null

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val memoRepository : MemoRepository by lazy {
        MemoRepositoryImpl(FirestoreMemoDataSourceImpl())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForegroundNotify()

        val memoIdList = intent?.getStringArrayListExtra("memoIds")
        serviceScope.launch {
            memoIdList?.let {
                it.forEach { memoId ->
                    memoRepository.getMemoById(memoId).collect { result ->
                        when (result) {
                            is DataResourceResult.Success -> {
                                showGeoFencingNotification(applicationContext, result.data)
                            }
                            is DataResourceResult.Failure -> { /* 실패 처리 */ }
                            is DataResourceResult.Loading -> { /* 로딩 처리 */ }
                        }
                    }
                }
            }
            stopGeoFencingNotification()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private lateinit var notificationManager: NotificationManager

    @SuppressLint("ForegroundServiceType")
    private fun startForegroundNotify() {
        notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            "geofence_channel",
            "Geofence Notifications",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = "Geofence 알림을 위한 채널"
        channel.enableLights(true)
        channel.enableVibration(true)
        channel.vibrationPattern = longArrayOf(100, 200, 100, 200)
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(this, "geofence_channel")
            .setSmallIcon(R.drawable.jinada_logo)
            .setContentTitle("지나다 알림 서비스")
            .setContentText("근처 메모를 확인하는 중입니다.")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    fun showGeoFencingNotification(context: Context, todoItemData: TodoItemData){
        val notifyIntent = Intent(
            Intent.ACTION_VIEW,
            "jinada://main".toUri()
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val notifyPendingIntent = PendingIntent.getActivity(
            context, 0, notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 알림 생성
        val notification = NotificationCompat.Builder(context, "geofence_channel")
            .setSmallIcon(R.drawable.jinada_logo)
            .setContentTitle("현재 위치에서 가까운 메모가 있어요.")
            .setContentText(todoItemData.content)
            .setWhen(System.currentTimeMillis())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(notifyPendingIntent)
            .setAutoCancel(true)
            .build()

        // 알림 표시
        notificationManager.notify(todoItemData.memoId.hashCode(), notification)
    }

    fun stopGeoFencingNotification(){
        notificationManager.cancel(NOTIFICATION_ID)
        if (serviceScope.isActive) serviceScope.cancel()
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopGeoFencingNotification()
    }
}