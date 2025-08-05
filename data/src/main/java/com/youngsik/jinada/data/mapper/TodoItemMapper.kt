package com.youngsik.jinada.data.mapper

import com.google.firebase.firestore.GeoPoint
import com.youngsik.domain.model.TodoItemData
import com.youngsik.jinada.data.dataclass.TodoItemDto
import com.youngsik.jinada.data.utils.changeToLocalDate
import com.youngsik.jinada.data.utils.changeToStringDate
import com.youngsik.jinada.data.utils.toLocalDate
import com.youngsik.jinada.data.utils.toStringDistance
import com.youngsik.jinada.data.utils.toTimestamp

fun TodoItemDto.toDomainModel(memoId: String, distance: Double = 0.0): TodoItemData {
    return TodoItemData(
        memoId = memoId,
        content = this.content,
        isCompleted = this.isCompleted,
        locationName = this.locationName,
        latitude = this.locationInfo?.latitude ?: 0.0,
        longitude = this.locationInfo?.longitude ?: 0.0,
        distance = distance,
        deadlineDate = changeToStringDate(this.deadlineDate.toLocalDate()),
        completeDate = if (this.completeDate != null) changeToStringDate(this.completeDate!!.toLocalDate()) else null
    )
}

fun TodoItemData.toDto(uuid: String,geohash: String): TodoItemDto {
    return TodoItemDto(
        uuid = uuid,
        content = this.content,
        isCompleted = this.isCompleted,
        locationName = this.locationName,
        locationInfo = GeoPoint(this.latitude, this.longitude),
        deadlineDate = changeToLocalDate(this.deadlineDate).toTimestamp(),
        completeDate = if (this.completeDate != null) changeToLocalDate(this.completeDate!!)?.toTimestamp() else null,
        geohash = geohash
    )
}