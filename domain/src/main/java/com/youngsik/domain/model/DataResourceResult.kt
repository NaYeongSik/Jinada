package com.youngsik.domain.model

sealed class DataResourceResult<out T> {
    data object Loading : DataResourceResult<Nothing>()
    data class Success<out T>(
        val data: T
    ) : DataResourceResult<T>()
    data class Failure(
        val exception: Throwable
    ) : DataResourceResult<Nothing>()
}