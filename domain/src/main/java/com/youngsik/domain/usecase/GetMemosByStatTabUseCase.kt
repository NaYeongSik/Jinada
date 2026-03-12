package com.youngsik.domain.usecase

import com.youngsik.domain.entity.DataResourceResult
import com.youngsik.domain.entity.TodoItemData
import com.youngsik.domain.repository.MemoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 선택된 통계 탭(Weekly, Monthly 등)에 따른 메모 목록을 가져오는 유즈케이스입니다.
 */
class GetMemosByStatTabUseCase @Inject constructor(
    private val memoRepository: MemoRepository
) {
    suspend operator fun invoke(selectedTabMenu: String, nickname: String): Flow<DataResourceResult<List<TodoItemData>>> {
        return memoRepository.getMemoListBySelectedStatTabMenu(selectedTabMenu, nickname)
    }
}
