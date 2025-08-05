package com.youngsik.jinada.manager

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.youngsik.domain.manager.GeoFencingManager
import com.youngsik.domain.model.TodoItemData
import com.youngsik.jinada.receiver.GeoFencingReceiver

class GeoFencingManagerImpl(private val context: Context) : GeoFencingManager{
    private val geofencingClient = LocationServices.getGeofencingClient(context)
    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeoFencingReceiver::class.java)
        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    override fun updateGeoPencing(memoList: List<TodoItemData>) {


        val geofenceList = memoList.map { memo ->
            Geofence.Builder()
                .setRequestId(memo.memoId)
                .setCircularRegion(
                    memo.latitude,
                    memo.longitude,
                    300f // TODO: 설정으로 저장된 알람 반경에 맞춰서 설정하도록 수정 (미터 단위)
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
                .build()
        }

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofences(geofenceList)
            .build()

        geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent)
        Log.d("jinada_test", "GeoFencingManagerImpl addGeoPencing: ${memoList.size}")
    }

    override fun removeGeoPencing(memo: TodoItemData) {
        // TODO: GeoFencing 등록 취소
    }
}