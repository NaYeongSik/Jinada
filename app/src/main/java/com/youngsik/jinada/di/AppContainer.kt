package com.youngsik.jinada.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.youngsik.jinada.data.datasource.DataStoreDataSource
import com.youngsik.jinada.data.datasource.local.DataStoreDataSourceImpl
import com.youngsik.jinada.data.datasource.remote.FirestoreMemoDataSourceImpl
import com.youngsik.jinada.data.impl.DataStoreRepositoryImpl
import com.youngsik.jinada.data.impl.MemoRepositoryImpl
import com.youngsik.jinada.data.repository.CurrentLocationRepository
import com.youngsik.jinada.data.repository.DataStoreRepository
import com.youngsik.jinada.data.repository.MemoRepository

interface AppContainer {
    val memoRepository: MemoRepository
    val locationRepository: CurrentLocationRepository
    val dataStoreRepository: DataStoreRepository
    val viewModelFactory: ViewModelProvider.Factory
}

class AppContainerImpl(private val applicationContext: Context) : AppContainer {
    override val memoRepository: MemoRepository by lazy {
        MemoRepositoryImpl(FirestoreMemoDataSourceImpl())
    }

    override val dataStoreRepository: DataStoreRepository by lazy {
        DataStoreRepositoryImpl(DataStoreDataSourceImpl(applicationContext))
    }
    override val locationRepository: CurrentLocationRepository by lazy {
        LocationRepositoryProvider.getInstance(applicationContext)
    }

    override val viewModelFactory: ViewModelProvider.Factory by lazy {
        ViewModelFactory(memoRepository, locationRepository, dataStoreRepository)
    }
}