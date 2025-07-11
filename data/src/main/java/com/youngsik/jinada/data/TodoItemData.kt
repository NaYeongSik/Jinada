package com.youngsik.jinada.data

import kotlinx.serialization.Serializable


@Serializable
data class TodoItemData(
    val id: Int = 0,
    val title: String = "",
    val isCompleted: Boolean = false,
    val storeInfo: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val distance: String = "",
    val deadlineDate: String = "",
    val completeDate: String = ""
)