package com.youngsik.jinada.common

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class JinadaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}