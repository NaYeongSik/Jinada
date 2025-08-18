package com.youngsik.domain.entity

data class UserSettings(
    val closerNotificationEnabled: Boolean = false,
    val dailyNotificationEnabled: Boolean = false,
    val closerMemoSearchingRange: Float = 0.3f,
    val closerMemoNotiRange: Float = 0.3f
)