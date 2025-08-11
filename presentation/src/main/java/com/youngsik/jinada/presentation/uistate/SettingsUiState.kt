package com.youngsik.jinada.presentation.uistate

data class SettingsUiState(
    val isLoading: Boolean = false,
    val lastSuccessfulAction: String = "",
    val isFailure: Boolean = false,
    val nickname: String = "",
    val uuid: String = "",
    val closerNotiEnabled: Boolean = false,
    val dailyNotiEnabled: Boolean = false,
    val closerMemoSearchingRange: Float = 0.3f,
    val closerMemoNotiRange: Float = 0.3f,
    val isNicknameAvailable: Boolean = false
)