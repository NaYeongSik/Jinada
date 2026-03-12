package com.youngsik.domain.usecase

import com.youngsik.domain.entity.DataResourceResult
import com.youngsik.domain.entity.TodoItemData
import com.youngsik.domain.repository.MemoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 기존 메모를 수정하는 유즈케이스입니다.
 */
class UpdateMemoUseCase @Inject constructor(
    private val memoRepository: MemoRepository
) {
    suspend operator fun invoke(todoItemData: TodoItemData, nickname: String): Flow<DataResourceResult<Unit>> {
        return memoRepository.updateMemo(todoItemData, nickname)
    }
}
