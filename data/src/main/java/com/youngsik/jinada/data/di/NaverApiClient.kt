package com.youngsik.jinada.data.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.youngsik.jinada.data.repository.NaverApiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NaverApiClient {
    private val NAVER_MAP_BASE_URL = "https://maps.apigw.ntruss.com/"
    private val NAVER_SEARCH_BASE_URL = "https://openapi.naver.com/"

    @Provides
    @Singleton
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    @Named("mapRetrofit")
    fun provideMapRetrofit(moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl(NAVER_MAP_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @Singleton
    @Named("searchRetrofit")
    fun provideSearchRetrofit(moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl(NAVER_SEARCH_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @Singleton
    @Named("mapApi")
    fun provideNaverMapApi(@Named("mapRetrofit") retrofit: Retrofit): NaverApiRepository =
        retrofit.create(NaverApiRepository::class.java)

    @Provides
    @Singleton
    @Named("searchApi")
    fun provideNaverSearchApi(@Named("searchRetrofit") retrofit: Retrofit): NaverApiRepository =
        retrofit.create(NaverApiRepository::class.java)
}