package com.youngsik.jinada.data.datasource


import android.location.Location
import com.youngsik.domain.model.DataResourceResult
import com.youngsik.domain.model.TodoItemData

interface MemoDataSource {
    suspend fun createMemo(todoItemData: TodoItemData): DataResourceResult<Unit>
    suspend fun updateMemo(todoItemData: TodoItemData): DataResourceResult<Unit>
    suspend fun deleteMemo(memoId: String): DataResourceResult<Unit>
    suspend fun getMemoById(memoId: String): DataResourceResult<TodoItemData>
    suspend fun getMemoListBySelectedDate(date: String): DataResourceResult<List<TodoItemData>>
    suspend fun getMemoListBySelectedStatTabMenu(selectedTabMenu: String): DataResourceResult<List<TodoItemData>>
    suspend fun getNearByMemoList(location: Location): DataResourceResult<List<TodoItemData>>
}