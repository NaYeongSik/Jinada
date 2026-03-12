package com.youngsik.domain.repository

import com.youngsik.domain.entity.DataResourceResult
import com.youngsik.domain.entity.LocationEntity
import com.youngsik.domain.entity.TodoItemData
import kotlinx.coroutines.flow.Flow

/**
 * 메모 관련 비즈니스 로직을 정의하는 인터페이스입니다.
 * 도메인 계층에 위치하여 외부 계층(Data, Presentation)과의 계약 역할을 합니다.
 */
interface MemoRepository {
    suspend fun createMemo(todoItemData: TodoItemData, nickname: String): Flow<DataResourceResult<Unit>>
    suspend fun updateMemo(todoItemData: TodoItemData, nickname: String): Flow<DataResourceResult<Unit>>
    suspend fun deleteMemo(memoId: String): Flow<DataResourceResult<Unit>>
    suspend fun getMemoById(memoId: String): Flow<DataResourceResult<TodoItemData>>
    suspend fun getMemoListBySelectedDate(date: String, nickname: String): Flow<DataResourceResult<List<TodoItemData>>>
    suspend fun getMemoListBySelectedStatTabMenu(selectedTabMenu: String, nickname: String): Flow<DataResourceResult<List<TodoItemData>>>
    
    /**
     * 현재 위치 정보를 기반으로 인접한 메모 목록을 가져옵니다.
     * [location] 파라미터는 도메인 엔티티인 LocationEntity를 사용합니다.
     */
    suspend fun getNearByMemoList(nickname: String, location: LocationEntity, range: Float): Flow<DataResourceResult<List<TodoItemData>>>
}
