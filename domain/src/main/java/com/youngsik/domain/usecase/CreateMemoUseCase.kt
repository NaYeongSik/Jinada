package com.youngsik.domain.usecase

import com.youngsik.domain.entity.DataResourceResult
import com.youngsik.domain.entity.TodoItemData
import com.youngsik.domain.repository.MemoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 새로운 메모를 생성하는 유즈케이스입니다.
 */
class CreateMemoUseCase @Inject constructor(
    private val memoRepository: MemoRepository
) {
    suspend operator fun invoke(todoItemData: TodoItemData, nickname: String): Flow<DataResourceResult<Unit>> {
        return memoRepository.createMemo(todoItemData, nickname)
    }
}
