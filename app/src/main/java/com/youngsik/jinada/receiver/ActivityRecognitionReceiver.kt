package com.youngsik.jinada.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionResult
import com.google.android.gms.location.DetectedActivity
import com.youngsik.jinada.service.ActivityRecognitionService

class ActivityRecognitionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == "ACTION_ACTIVITY_TRANSITION_UPDATE") {
            if (ActivityTransitionResult.hasResult(intent)) {
                val result = ActivityTransitionResult.extractResult(intent)
                result?.transitionEvents?.forEach { event ->
                    val serviceIntent = Intent(context, ActivityRecognitionService::class.java)

                    if (isMovingEvent(event.activityType) && event.transitionType == ActivityTransition.ACTIVITY_TRANSITION_ENTER
                    ) {
                        serviceIntent.action = ActivityRecognitionService.ACTION_START_TRACKING
                        context.startForegroundService(serviceIntent)
                    } else if (isMovingEvent(event.activityType) && event.transitionType == ActivityTransition.ACTIVITY_TRANSITION_ENTER
                    ) {
                        serviceIntent.action = ActivityRecognitionService.ACTION_STOP_TRACKING
                        context.startService(serviceIntent)
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