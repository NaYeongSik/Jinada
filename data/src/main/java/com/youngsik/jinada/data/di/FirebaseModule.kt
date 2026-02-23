package com.youngsik.jinada.data.di

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    @Singleton
    @Named("memoCollection")
    fun provideMemoCollection(firestore: FirebaseFirestore): CollectionReference = firestore.collection("memo")

    @Provides
    @Singleton
    @Named("userCollection")
    fun provideUserCollection(firestore: FirebaseFirestore): CollectionReference = firestore.collection("user_info")

}