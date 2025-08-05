package com.youngsik.domain.model

data class TodoItemData(
    val memoId: String = "",
    val content: String = "",
    val isCompleted: Boolean = false,
    val locationName: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val distance: Double = 0.0,
    val deadlineDate: String = "",
    val completeDate: String? = null
)