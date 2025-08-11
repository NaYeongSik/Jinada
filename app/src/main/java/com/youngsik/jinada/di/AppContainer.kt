package com.youngsik.jinada.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.youngsik.jinada.data.datasource.DataStoreDataSource
import com.youngsik.jinada.data.datasource.local.DataStoreDataSourceImpl
import com.youngsik.jinada.data.datasource.remote.FirestoreMemoDataSourceImpl
import com.youngsik.jinada.data.datasource.remote.FirestoreUserDataSourceImpl
import com.youngsik.jinada.data.impl.DataStoreRepositoryImpl
import com.youngsik.jinada.data.impl.MemoRepositoryImpl
import com.youngsik.jinada.data.impl.NaverRepositoryImpl
import com.youngsik.jinada.data.impl.UserRepositoryImpl
import com.youngsik.jinada.data.repository.CurrentLocationRepository
import com.youngsik.jinada.data.repository.DataStoreRepository
import com.youngsik.jinada.data.repository.MemoRepository
import com.youngsik.jinada.data.repository.NaverRepository
import com.youngsik.jinada.data.repository.UserRepository

interface AppContainer {
    val userRepository: UserRepository
    val memoRepository: MemoRepository
    val locationRepository: CurrentLocationRepository
    val dataStoreRepository: DataStoreRepository
    val naverRepository: NaverRepository
    val viewModelFactory: ViewModelProvider.Factory
}

class AppContainerImpl(private val applicationContext: Context) : AppContainer {
    override val userRepository: UserRepository by lazy {
        UserRepositoryImpl(FirestoreUserDataSourceImpl())
    }
    override val memoRepository: MemoRepository by lazy {
        MemoRepositoryImpl(FirestoreMemoDataSourceImpl())
    }
    override val dataStoreRepository: DataStoreRepository by lazy {
        DataStoreRepositoryImpl(DataStoreDataSourceImpl(applicationContext))
    }
    override val locationRepository: CurrentLocationRepository by lazy {
        LocationRepositoryProvider.getInstance(applicationContext)
    }
    override val naverRepository: NaverRepository by lazy {
        NaverRepositoryImpl()
    }
    override val viewModelFactory: ViewModelProvider.Factory by lazy {
        ViewModelFactory(userRepository,memoRepository, locationRepository, dataStoreRepository, naverRepository)
    }
}