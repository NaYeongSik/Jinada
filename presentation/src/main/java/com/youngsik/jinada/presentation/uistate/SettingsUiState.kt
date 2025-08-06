package com.youngsik.jinada.presentation.uistate

data class SettingsUiState(
    val isLoading: Boolean = false,
    val isSuccessful: Boolean = false,
    val isFailure: Boolean = false,
    val nickname: String = "ERROR",
    val uuid: String = "Not Found",
    val notificationEnabled: Boolean = false
)