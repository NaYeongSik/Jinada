package com.youngsik.jinada.data.repository

import android.location.Location
import com.youngsik.shared.model.DataResourceResult
import com.youngsik.domain.entity.TodoItemData
import kotlinx.coroutines.flow.Flow

interface MemoRepository {
    suspend fun createMemo(todoItemData: TodoItemData,nickname: String) : Flow<DataResourceResult<Unit>>
    suspend fun updateMemo(todoItemData: TodoItemData,nickname: String) : Flow<DataResourceResult<Unit>>
    suspend fun deleteMemo(memoId: String) : Flow<DataResourceResult<Unit>>
    suspend fun getMemoById(memoId: String) : Flow<DataResourceResult<TodoItemData>>
    suspend fun getMemoListBySelectedDate(date: String,nickname: String) : Flow<DataResourceResult<List<TodoItemData>>>
    suspend fun getMemoListBySelectedStatTabMenu(selectedTabMenu: String,nickname: String) : Flow<DataResourceResult<List<TodoItemData>>>
    suspend fun getNearByMemoList(nickname: String,location: Location,range: Float) : Flow<DataResourceResult<List<TodoItemData>>>
}