package com.youngsik.jinada.presentation.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.youngsik.jinada.presentation.R
import com.youngsik.jinada.shared.composable.CommonCard
import com.youngsik.jinada.shared.composable.CommonDividingLine
import com.youngsik.jinada.shared.composable.CommonSettingsDialog
import com.youngsik.jinada.shared.composable.CommonSliderOptionRow
import com.youngsik.jinada.shared.composable.CommonSwitchOptionRow
import com.youngsik.jinada.shared.composable.ListItemRow
import com.youngsik.jinada.shared.data.SettingOptionData
import com.youngsik.jinada.shared.data.SettingsData
import com.youngsik.jinada.shared.theme.JinadaDimens


@Composable
fun SettingsSection(onClickEvent: (String) -> Unit){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(JinadaDimens.Padding.medium),
        verticalArrangement = Arrangement.spacedBy(JinadaDimens.Spacer.large)
    ) {
        SettingsGroup(stringResource(R.string.profile_setting_title), SettingsData.myInfoItems,onClickEvent)

        SettingsGroup(stringResource(R.string.notification_setting_title), SettingsData.appSettingsItems,onClickEvent)

        SettingsGroup(stringResource(R.string.help_title), SettingsData.supportItems,onClickEvent)
    }
}

@Composable
fun SettingsGroup(title: String,optionList: List<SettingOptionData>, onClickEvent: (String)-> Unit) {
    CommonCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(JinadaDimens.Padding.medium),
            verticalArrangement = Arrangement.spacedBy(JinadaDimens.Spacer.xSmall)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold
            )
            CommonDividingLine(modifier = Modifier.fillMaxWidth())

            optionList.forEachIndexed { index, it ->
                ListItemRow(
                    icon = it.icon,
                    onClick = { onClickEvent(it.route) }
                ) {
                    Text(
                        text = it.text,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                if (index < optionList.lastIndex) {
                    CommonDividingLine(modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@Composable
fun DialogView(dialogRoute: String, onChangedDialogType:(String)-> Unit){
    // TODO: 저장된 설정 옵션 값으로 변경하기
    var (isCheckedCloserNoti,onCloserSwitchChanged) = remember { mutableStateOf(true) }
    var (isCheckedDailyNoti, onDailySwitchChanged) = remember { mutableStateOf(false) }
    var closerMemoRange by remember { mutableFloatStateOf(0.3f) }
    var closerNotiRange by remember { mutableFloatStateOf(0.3f) }

    when(dialogRoute){
        "NotificationSetting" -> {
            CommonSettingsDialog(
                stringResource(R.string.notification_setting_title),
                { onChangedDialogType("None") },
                { /*TODO: 저장시 동작*/ }) {
                Column {
                    CommonSwitchOptionRow(isCheckedCloserNoti, onCloserSwitchChanged) {
                        Text(
                            text = stringResource(R.string.notification_setting_closer_title),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = stringResource(R.string.notification_setting_closer_description),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    CommonDividingLine(Modifier.fillMaxWidth())

                    CommonSwitchOptionRow(isCheckedDailyNoti, onDailySwitchChanged) {
                        Text(
                            text = stringResource(R.string.notification_setting_daily_title),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = stringResource(R.string.notification_setting_daily_description),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
        "SearchRangeSetting" -> {
            CommonSettingsDialog(
                stringResource(R.string.setting_title_search_range),
                { onChangedDialogType("None") },
                { /*TODO: 저장시 동작*/ }) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CommonSliderOptionRow(closerMemoRange,
                        { newValue -> closerMemoRange = newValue }) {
                        Text(text = stringResource(R.string.search_range_setting_memo_title))
                        Text(text = "${(closerMemoRange * 1000).toInt()}m")
                    }

                    CommonDividingLine(modifier = Modifier.fillMaxWidth())

                    CommonSliderOptionRow(closerNotiRange,
                        { newValue -> closerNotiRange = newValue }) {
                        Text(text = stringResource(R.string.search_range_setting_notification_title))
                        Text(text = "${(closerNotiRange * 1000).toInt()}m")
                    }
                }
            }
        }
        else -> { }
    }
}