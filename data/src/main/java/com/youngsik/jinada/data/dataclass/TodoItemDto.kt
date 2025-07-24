package com.youngsik.jinada.data.dataclass

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.PropertyName

data class TodoItemDto(
    @get:PropertyName("uuid") @set:PropertyName("uuid")
    var uuid: String = "",
    @get:PropertyName("content") @set:PropertyName("content")
    var content: String = "",
    @get:PropertyName("completed") @set:PropertyName("completed")
    var isCompleted: Boolean = false,
    @get:PropertyName("location_name") @set:PropertyName("location_name")
    var locationName: String = "",

    @get:PropertyName("location_info") @set:PropertyName("location_info")
    var locationInfo: GeoPoint? = null,

    @get:PropertyName("deadline_date") @set:PropertyName("deadline_date")
    var deadlineDate: Timestamp = Timestamp.now(),

    @get:PropertyName("complete_date") @set:PropertyName("complete_date")
    var completeDate: Timestamp? = null
)