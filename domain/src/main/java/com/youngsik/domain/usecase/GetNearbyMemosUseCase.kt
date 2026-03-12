package com.youngsik.domain.usecase

import com.youngsik.domain.entity.DataResourceResult
import com.youngsik.domain.entity.LocationEntity
import com.youngsik.domain.entity.TodoItemData
import com.youngsik.domain.repository.MemoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * 현재 위치를 기준으로 일정 거리 내에 있는 메모 목록을 가져오는 유즈케이스입니다.
 */
class GetNearbyMemosUseCase @Inject constructor(
    private val memoRepository: MemoRepository
) {
    /**
     * [nickname] 사용자의 메모 중 [location] 기준 [range] 이내의 메모를 조회합니다.
     * 조회된 메모는 거리순으로 정렬됩니다.
     */
    suspend operator fun invoke(
        nickname: String,
        location: LocationEntity,
        range: Float
    ): Flow<DataResourceResult<List<TodoItemData>>> {
        return memoRepository.getNearByMemoList(nickname, location, range).map { result ->
            when (result) {
                is DataResourceResult.Success -> {
                    // 성공 시 거리순으로 정렬하는 비즈니스 규칙 적용
                    DataResourceResult.Success(result.data.sortedBy { it.distance })
                }
                else -> result
            }
        }
    }
}
