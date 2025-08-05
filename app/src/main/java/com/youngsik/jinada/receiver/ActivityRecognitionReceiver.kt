package com.youngsik.jinada.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionResult
import com.google.android.gms.location.DetectedActivity
import com.youngsik.jinada.service.ActivityRecognitionService

class ActivityRecognitionReceiver : BroadcastReceiver() {
    private lateinit var targetIntent : Intent
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == "ACTION_ACTIVITY_TRANSITION_UPDATE") {
            if (ActivityTransitionResult.hasResult(intent)) {
                if (!::targetIntent.isInitialized) {
                    targetIntent = Intent(context, ActivityRecognitionService::class.java)
                }
                val result = ActivityTransitionResult.extractResult(intent)
                result?.transitionEvents?.forEach { event -> // TODO: forEach 제거

                    if (isMovingEvent(event.activityType) && event.transitionType == ActivityTransition.ACTIVITY_TRANSITION_ENTER
                    ) {
                        Log.d("jinada_test", "ActivityRecognitionReceiver ${event.activityType}")
                        targetIntent.action = ActivityRecognitionService.Companion.ACTION_UPDATE_GEOFENCING
                        context.startForegroundService(targetIntent)
                    }
                }
            }
        }
    }

    private fun isMovingEvent(activityType: Int)= when (activityType) {
            DetectedActivity.IN_VEHICLE,
            DetectedActivity.ON_BICYCLE,
            DetectedActivity.ON_FOOT,
            DetectedActivity.RUNNING,
            DetectedActivity.WALKING -> true
            else -> false
        }

}