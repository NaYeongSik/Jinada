package com.youngsik.jinada.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.youngsik.jinada.presentation.service.GeoFensingNotificationService

class GeoFencingReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent?.hasError() == true) {
            return
        }
        if (geofencingEvent?.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            val triggeredMemoIds = geofencingEvent.triggeringGeofences?.map { it.requestId }
            val serviceIntent = Intent(context, GeoFensingNotificationService::class.java)
            serviceIntent.putStringArrayListExtra("memoIds", ArrayList(triggeredMemoIds))
            context.startForegroundService(serviceIntent)
        }

    }
}