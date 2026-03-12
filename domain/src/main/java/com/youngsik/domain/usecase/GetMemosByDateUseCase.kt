package com.youngsik.domain.usecase

import com.youngsik.domain.entity.DataResourceResult
import com.youngsik.domain.entity.TodoItemData
import com.youngsik.domain.repository.MemoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 특정 날짜의 메모 목록을 가져오는 유즈케이스입니다.
 */
class GetMemosByDateUseCase @Inject constructor(
    private val memoRepository: MemoRepository
) {
    suspend operator fun invoke(date: String, nickname: String): Flow<DataResourceResult<List<TodoItemData>>> {
        return memoRepository.getMemoListBySelectedDate(date, nickname)
    }
}
