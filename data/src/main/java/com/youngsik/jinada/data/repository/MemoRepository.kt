package com.youngsik.jinada.data.repository

import com.naver.maps.geometry.LatLng
import com.youngsik.jinada.data.common.DataResourceResult
import com.youngsik.jinada.data.dataclass.TodoItemData
import kotlinx.coroutines.flow.Flow

interface MemoRepository {
    suspend fun createMemo(todoItemData: TodoItemData) : Flow<DataResourceResult<Unit>>
    suspend fun updateMemo(todoItemData: TodoItemData) : Flow<DataResourceResult<Unit>>
    suspend fun deleteMemo(memoId: String) : Flow<DataResourceResult<Unit>>
    suspend fun getMemoListBySelectedDate(date: String) : Flow<DataResourceResult<List<TodoItemData>>>
    suspend fun getMemoListBySelectedStatTabMenu(selectedTabMenu: String) : Flow<DataResourceResult<List<TodoItemData>>>
    suspend fun getNearByMemoList(location: LatLng) : Flow<DataResourceResult<List<TodoItemData>>>
}