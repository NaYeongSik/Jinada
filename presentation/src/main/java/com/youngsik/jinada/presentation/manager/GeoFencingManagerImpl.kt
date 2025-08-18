package com.youngsik.jinada.presentation.manager

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.youngsik.domain.manager.GeoFencingManager
import com.youngsik.domain.entity.TodoItemData
import com.youngsik.jinada.presentation.receiver.GeoFencingReceiver

class GeoFencingManagerImpl(private val context: Context) : GeoFencingManager{
    private val geofencingClient = LocationServices.getGeofencingClient(context)
    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeoFencingReceiver::class.java)
        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    override fun updateGeoPencing(memoList: List<TodoItemData>, notiRange: Float) {
        geofencingClient.removeGeofences(geofencePendingIntent) // 이전 등록된 지오펜싱 이벤트 제거 (최대 100개 이벤트만 등록 가능하기때문에)

        val geofenceList = memoList.map { memo ->
            Geofence.Builder()
                .setRequestId(memo.memoId)
                .setCircularRegion(
                    memo.latitude,
                    memo.longitude,
                    notiRange * 1000 // 미터 단위
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
                .build()
        }

        if (geofenceList.isEmpty()) return

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofences(geofenceList)
            .build()

        geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent)
    }

    override fun removeGeoPencing() {
        geofencingClient.removeGeofences(geofencePendingIntent)
    }
}