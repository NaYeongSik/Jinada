package com.youngsik.domain.entity

/**
 * 데이터 요청의 결과 상태를 정의하는 제네릭 클래스입니다.
 * 도메인 계층에 위치하여 모든 모듈이 공통 계약으로 사용합니다.
 */
sealed class DataResourceResult<out T> {
    data object Loading : DataResourceResult<Nothing>()
    data class Success<out T>(
        val data: T
    ) : DataResourceResult<T>()
    data class Failure(
        val exception: Throwable
    ) : DataResourceResult<Nothing>()
}
