package com.youngsik.jinada.data.dataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class TodoItemData(
    val memoId: String = "",
    val content: String = "",
    val isCompleted: Boolean = false,
    val locationName: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val distance: String = "",
    val deadlineDate: String = "",
    val completeDate: String? = null
) : Parcelable