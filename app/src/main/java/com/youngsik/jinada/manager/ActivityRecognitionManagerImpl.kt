package com.youngsik.jinada.manager

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionRequest
import com.google.android.gms.location.DetectedActivity
import com.youngsik.domain.manager.ActivityRecognitionManager
import com.youngsik.jinada.receiver.ActivityRecognitionReceiver

class ActivityRecognitionManagerImpl(private val context: Context) : ActivityRecognitionManager{

    companion object{
        const val ACTION_ACTIVITY_TRANSITION_UPDATE = "ACTION_ACTIVITY_TRANSITION_UPDATE"
    }

    @RequiresPermission(anyOf = [Manifest.permission.ACTIVITY_RECOGNITION, "com.google.android.gms.permission.ACTIVITY_RECOGNITION"])
    override fun startActivityRecognition() {
        val intent = Intent(context, ActivityRecognitionReceiver::class.java).apply {
            action = ACTION_ACTIVITY_TRANSITION_UPDATE
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        val transitions = listOf(
            // 감지할 활동들을 정의
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.WALKING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build(),
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.IN_VEHICLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build(),
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.RUNNING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build(),
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.ON_FOOT)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build()
        )
        val request = ActivityTransitionRequest(transitions)

        ActivityRecognition.getClient(context).requestActivityTransitionUpdates(request, pendingIntent).addOnSuccessListener {
            Log.d("jinada_test", "startActivityRecognition: success")

        }.addOnFailureListener {
            Log.d("jinada_test", "startActivityRecognition: fail")
        }
    }

    @RequiresPermission(anyOf = [Manifest.permission.ACTIVITY_RECOGNITION, "com.google.android.gms.permission.ACTIVITY_RECOGNITION"])
    override fun stopActivityRecognition() {
        ActivityRecognition.getClient(context).removeActivityTransitionUpdates(PendingIntent.getBroadcast(context, 0, Intent(context, ActivityRecognitionReceiver::class.java), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE))
    }

}