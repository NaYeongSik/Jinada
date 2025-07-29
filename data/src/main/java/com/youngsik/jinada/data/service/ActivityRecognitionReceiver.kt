package com.youngsik.jinada.data.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionResult
import com.google.android.gms.location.DetectedActivity

class ActivityRecognitionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == "ACTION_ACTIVITY_TRANSITION_UPDATE") {
            if (ActivityTransitionResult.hasResult(intent)) {
                val result = ActivityTransitionResult.extractResult(intent)
                result?.transitionEvents?.forEach { event ->
                    val serviceIntent = Intent(context, ActivityRecognitionService::class.java)

                    if (isMovingEvent(event.activityType) && event.transitionType == ActivityTransition.ACTIVITY_TRANSITION_ENTER
                    ) {
                        Toast.makeText(context, "ActivityRecognitionReceiver ${event.activityType}", Toast.LENGTH_SHORT).show()
                        Log.d("jinada_test", "ActivityRecognitionReceiver ${event.activityType}")
                        serviceIntent.action = ActivityRecognitionService.ACTION_UPDATE_GEOFENCING
                        context.startForegroundService(serviceIntent)
                    }
                }
            }
        }
    }

    private fun isMovingEvent(activityType: Int): Boolean {
        when (activityType) {
            DetectedActivity.IN_VEHICLE,
            DetectedActivity.ON_BICYCLE,
            DetectedActivity.ON_FOOT,
            DetectedActivity.RUNNING,
            DetectedActivity.WALKING -> return true
            else -> return false
        }
    }

}