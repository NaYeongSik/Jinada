package com.youngsik.jinada.shared.data

import kotlinx.serialization.Serializable


@Serializable
data class TodoItemData(
    val id: Int = 0,
    val title: String = "",
    val isCompleted: Boolean = false,
    val storeInfo: String = "",
    val distance: String = "",
    val deadlineDate: String = "",
    val completeDate: String = ""
)