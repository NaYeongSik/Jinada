package com.youngsik.jinada.data.di

import com.youngsik.jinada.data.datasource.DataStoreDataSource
import com.youngsik.jinada.data.datasource.MemoDataSource
import com.youngsik.jinada.data.datasource.UserDataSource
import com.youngsik.jinada.data.datasource.local.DataStoreDataSourceImpl
import com.youngsik.jinada.data.datasource.remote.FirestoreMemoDataSourceImpl
import com.youngsik.jinada.data.datasource.remote.FirestoreUserDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindUserDataSource(userDataSourceImpl: FirestoreUserDataSourceImpl): UserDataSource

    @Binds
    @Singleton
    abstract fun bindMemoDataSource(memoDataSourceImpl: FirestoreMemoDataSourceImpl): MemoDataSource

    @Binds
    @Singleton
    abstract fun bindDataStoreDataSource(dataStoreDataSourceImpl: DataStoreDataSourceImpl): DataStoreDataSource
}