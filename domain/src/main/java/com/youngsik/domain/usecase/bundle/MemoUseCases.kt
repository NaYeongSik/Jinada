package com.youngsik.domain.usecase.bundle

import com.youngsik.domain.usecase.CreateMemoUseCase
import com.youngsik.domain.usecase.DeleteMemoUseCase
import com.youngsik.domain.usecase.GetMemoStatisticsUseCase
import com.youngsik.domain.usecase.GetMemosByDateUseCase
import com.youngsik.domain.usecase.GetMemosByStatTabUseCase
import com.youngsik.domain.usecase.GetNearbyMemosUseCase
import com.youngsik.domain.usecase.UpdateMemoUseCase
import javax.inject.Inject

/**
 * 메모 관련 모든 유즈케이스를 하나로 묶어주는 번들 클래스입니다.
 */
class MemoUseCases @Inject constructor(
    val createMemo: CreateMemoUseCase,
    val updateMemo: UpdateMemoUseCase,
    val deleteMemo: DeleteMemoUseCase,
    val getMemosByDate: GetMemosByDateUseCase,
    val getNearbyMemos: GetNearbyMemosUseCase,
    val getMemosByStatTab: GetMemosByStatTabUseCase,
    val getMemoStatistics: GetMemoStatisticsUseCase
)
