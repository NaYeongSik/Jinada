package com.youngsik.jinada.data.remote

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.naver.maps.geometry.LatLng
import com.youngsik.jinada.data.common.DataResourceResult
import com.youngsik.jinada.data.dataclass.TodoItemData
import com.youngsik.jinada.data.dataclass.TodoItemDto
import com.youngsik.jinada.data.datasource.MemoDataSource
import com.youngsik.jinada.data.mapper.toDomainModel
import com.youngsik.jinada.data.mapper.toDto
import com.youngsik.jinada.data.utils.changeToLocalDate
import com.youngsik.jinada.data.utils.toTimestamp
import kotlinx.coroutines.tasks.await

class FirestoreMemoDataSourceImpl : MemoDataSource {
    private val memoCollection = Firebase.firestore.collection("memo")
    private val userId = "EgQ5odyd7yj0yxcactCv" // TODO: sharedPreference에 저장된 uuid 가져오기

    override suspend fun createMemo(todoItemData: TodoItemData): DataResourceResult<Unit> = runCatching {
        memoCollection.add(todoItemData.toDto(userId)).await()
        DataResourceResult.Success(Unit)
    }.getOrElse {
        DataResourceResult.Failure(it)
    }

    override suspend fun updateMemo(todoItemData: TodoItemData): DataResourceResult<Unit> = runCatching {
        memoCollection.document(todoItemData.memoId).set(todoItemData.toDto(userId)).await()
        DataResourceResult.Success(Unit)
    }.getOrElse {
        DataResourceResult.Failure(it)
    }

    override suspend fun deleteMemo(memoId: String): DataResourceResult<Unit> = runCatching {
        memoCollection.document(memoId).delete().await()
        DataResourceResult.Success(Unit)
    }.getOrElse {
        DataResourceResult.Failure(it)
    }

    override suspend fun getMemoListBySelectedDate(date: String): DataResourceResult<List<TodoItemData>> = runCatching {
        val snapShot = memoCollection.whereEqualTo("uuid", userId)
            .whereGreaterThanOrEqualTo("deadline_date", changeToLocalDate(date).toTimestamp())
            .get().await()

        val memoList = snapShot.documents.mapNotNull { document ->
            document.toObject(TodoItemDto::class.java)?.toDomainModel(document.id)
        }

        val filteredList = memoList.filter { memo ->
            if (memo.isCompleted) changeToLocalDate(memo.completeDate!!) >= changeToLocalDate(date)
            else true
        }
        DataResourceResult.Success(filteredList)
    }.getOrElse { DataResourceResult.Failure(it) }

    override suspend fun getMemoListBySelectedStatTabMenu(selectedTabMenu: String): DataResourceResult<List<TodoItemData>> {
        TODO("Not yet implemented")
    }

    override suspend fun getNearByMemoList(location: LatLng): DataResourceResult<List<TodoItemData>> {
        TODO("Not yet implemented")
    }
}