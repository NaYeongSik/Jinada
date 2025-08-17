package com.youngsik.jinada.presentation.provider

import android.content.Context
import com.youngsik.jinada.data.impl.CurrentLocationRepositoryImpl
import com.youngsik.jinada.data.repository.CurrentLocationRepository

object LocationRepositoryProvider {

    @Volatile
    private var instance: CurrentLocationRepository? = null

    fun getInstance(context: Context): CurrentLocationRepository {
        return instance ?: synchronized(this) {
            val newInstance = CurrentLocationRepositoryImpl(context.applicationContext)
            instance = newInstance
            newInstance
        }
    }
}