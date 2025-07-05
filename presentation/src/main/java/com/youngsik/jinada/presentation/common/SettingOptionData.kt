package com.youngsik.jinada.presentation.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.QuestionAnswer
import androidx.compose.ui.graphics.vector.ImageVector

data class SettingOptionData(
    val text: String,
    val icon: ImageVector,
    val route: SettingDialogState
)

object SettingsData{
    val myInfoItems = listOf(
        SettingOptionData("내 정보 수정", Icons.Outlined.Person, SettingDialogState.UPDATE_INFO)
    )

    val appSettingsItems = listOf(
        SettingOptionData("알림 설정", Icons.Outlined.Notifications, SettingDialogState.NOTIFICATION_RANGE),
        SettingOptionData("메모 검색 범위 설정", Icons.Outlined.MyLocation, SettingDialogState.SEARCHING_RANGE)
    )

    val supportItems = listOf(
        SettingOptionData("도움말", Icons.Outlined.QuestionAnswer, SettingDialogState.HELP)
    )
}

