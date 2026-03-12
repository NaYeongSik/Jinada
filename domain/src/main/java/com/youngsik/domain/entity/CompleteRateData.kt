package com.youngsik.domain.entity

/**
 * 메모 완료율 데이터를 담는 비즈니스 엔티티입니다.
 * 통계 계산 결과(비율, 전체 개수, 성공 개수)를 정의합니다.
 */
data class CompleteRateData(
    val rate: Float,
    val totalCount: Int,
    val successCount: Int
)
