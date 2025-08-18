package com.youngsik.jinada.data.datasource


import android.location.Location
import com.youngsik.shared.model.DataResourceResult
import com.youngsik.domain.entity.TodoItemData

interface MemoDataSource {
    suspend fun createMemo(todoItemData: TodoItemData,nickname: String): DataResourceResult<Unit>
    suspend fun updateMemo(todoItemData: TodoItemData,nickname: String): DataResourceResult<Unit>
    suspend fun deleteMemo(memoId: String): DataResourceResult<Unit>
    suspend fun getMemoById(memoId: String): DataResourceResult<TodoItemData>
    suspend fun getMemoListBySelectedDate(date: String,nickname: String): DataResourceResult<List<TodoItemData>>
    suspend fun getMemoListBySelectedStatTabMenu(selectedTabMenu: String,nickname: String): DataResourceResult<List<TodoItemData>>
    suspend fun getNearByMemoList(nickname: String,location: Location, range: Float): DataResourceResult<List<TodoItemData>>
}