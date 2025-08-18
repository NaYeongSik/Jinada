package com.youngsik.jinada.presentation.data

import com.youngsik.domain.entity.TodoItemData

fun TodoItemData.toParcelable(): TodoItemParcelable {
    return TodoItemParcelable(
        memoId = this.memoId,
        content = this.content,
        isCompleted = this.isCompleted,
        locationName = this.locationName,
        latitude = this.latitude,
        longitude = this.longitude,
        deadlineDate = this.deadlineDate,
        completeDate = this.completeDate
    )
}

fun TodoItemParcelable.toDomainModel(): TodoItemData {
    return TodoItemData(
        memoId = this.memoId,
        content = this.content,
        isCompleted = this.isCompleted,
        locationName = this.locationName,
        latitude = this.latitude,
        longitude = this.longitude,
        deadlineDate = this.deadlineDate,
        completeDate = this.completeDate
    )
}