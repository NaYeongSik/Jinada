package com.youngsik.jinada.data.datasource

import com.google.type.LatLng
import com.youngsik.jinada.data.common.DataResourceResult
import com.youngsik.jinada.data.dataclass.TodoItemData
import java.time.LocalDate

interface MemoDataSource {
    suspend fun createMemo(todoItemData: TodoItemData): DataResourceResult<Unit>
    suspend fun updateMemo(todoItemData: TodoItemData): DataResourceResult<Unit>
    suspend fun deleteMemo(memoId: String): DataResourceResult<Unit>
    suspend fun getMemoListBySelectedDate(date: String): DataResourceResult<List<TodoItemData>>
    suspend fun getMemoListBySelectedStatTabMenu(selectedTabMenu: String): DataResourceResult<List<TodoItemData>>
    suspend fun getNearByMemoList(location: LatLng): DataResourceResult<List<TodoItemData>>
}