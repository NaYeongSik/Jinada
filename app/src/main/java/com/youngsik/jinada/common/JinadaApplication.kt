package com.youngsik.jinada.common

import android.app.Application
import com.youngsik.jinada.di.AppContainer
import com.youngsik.jinada.di.AppContainerImpl

class JinadaApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }
}