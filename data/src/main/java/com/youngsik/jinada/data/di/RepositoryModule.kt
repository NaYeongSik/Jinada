package com.youngsik.jinada.data.di

import com.youngsik.jinada.data.impl.DataStoreRepositoryImpl
import com.youngsik.jinada.data.impl.MemoRepositoryImpl
import com.youngsik.jinada.data.impl.NaverRepositoryImpl
import com.youngsik.jinada.data.impl.UserRepositoryImpl
import com.youngsik.jinada.data.repository.DataStoreRepository
import com.youngsik.jinada.data.repository.MemoRepository
import com.youngsik.jinada.data.repository.NaverRepository
import com.youngsik.jinada.data.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindMemoRepository(memoRepositoryImpl: MemoRepositoryImpl): MemoRepository

    @Binds
    @Singleton
    abstract fun bindDataStoreRepository(dataStoreRepositoryImpl: DataStoreRepositoryImpl): DataStoreRepository

    @Binds
    @Singleton
    abstract fun bindNaverRepository(naverRepositoryImpl: NaverRepositoryImpl): NaverRepository
}