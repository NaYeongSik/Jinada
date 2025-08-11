package com.youngsik.jinada.data.datasource.remote

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.youngsik.domain.model.DataResourceResult
import com.youngsik.domain.model.UserInfo
import com.youngsik.jinada.data.dataclass.UserInfoDto
import com.youngsik.jinada.data.datasource.UserDataSource
import com.youngsik.jinada.data.mapper.toDomainModel
import com.youngsik.jinada.data.mapper.toDto
import kotlinx.coroutines.tasks.await

class FirestoreUserDataSourceImpl: UserDataSource {
    private val userCollection = Firebase.firestore.collection("user_info")

    override suspend fun createUserInfo(userInfo: UserInfo): DataResourceResult<Unit> = runCatching{
        userCollection.add(userInfo.toDto()).await()
        DataResourceResult.Success(Unit)
    }.getOrElse { DataResourceResult.Failure(it) }

    override suspend fun updateUserInfo(userInfo: UserInfo): DataResourceResult<Unit> = runCatching{
        userCollection.document(userInfo.uuid).set(userInfo.toDto()).await()
        DataResourceResult.Success(Unit)
    }.getOrElse { DataResourceResult.Failure(it) }

    override suspend fun getUserInfo(uuid: String): DataResourceResult<UserInfo> = runCatching{
        val user = userCollection.document(uuid).get().await()
        val dto = user.toObject(UserInfoDto::class.java)
        if (dto != null){
            DataResourceResult.Success(dto.toDomainModel())
        } else {
            DataResourceResult.Failure(Exception("dto is null"))
        }
    }.getOrElse { DataResourceResult.Failure(it) }

    override suspend fun isNicknameAvailable(nickname: String): DataResourceResult<Boolean> = runCatching{
        val result = userCollection.whereEqualTo("nickname", nickname).get().await().isEmpty
        DataResourceResult.Success(result)
    }.getOrElse { DataResourceResult.Failure(it) }
}