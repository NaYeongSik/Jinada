package com.youngsik.jinada.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.youngsik.jinada.service.GeoFensingNotificationService

class GeoFencingReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("jinada_test", "GeoFencingReceiver onReceive")
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent?.hasError() == true) {
            // TODO: 에러 처리
            return
        }
        if (geofencingEvent?.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            Log.d("jinada_test", "GeoFencingReceiver GEOFENCE_TRANSITION_ENTER: ${geofencingEvent.triggeringGeofences?.size}")
            val triggeredMemoIds = geofencingEvent.triggeringGeofences?.map { it.requestId }
            val serviceIntent = Intent(context, GeoFensingNotificationService::class.java)
            serviceIntent.putStringArrayListExtra("memoIds", ArrayList(triggeredMemoIds))
            context.startForegroundService(serviceIntent)
        }

    }
}