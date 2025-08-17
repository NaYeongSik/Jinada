package com.youngsik.jinada.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youngsik.domain.entity.TodoItemData
import com.youngsik.jinada.presentation.component.DatePickerModal
import com.youngsik.jinada.presentation.common.DatePickerSelectableDates
import com.youngsik.jinada.presentation.common.MemoWriteTabMenu
import com.youngsik.jinada.presentation.component.CommonCard
import com.youngsik.jinada.presentation.component.CommonDividingLine
import com.youngsik.jinada.presentation.component.CommonTabRow
import com.youngsik.jinada.presentation.component.ListItemRow
import com.youngsik.jinada.presentation.component.commonTabRow
import com.youngsik.shared.theme.JinadaDimens
import com.youngsik.jinada.presentation.viewmodel.MemoViewModel
import com.youngsik.shared.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoWriteScreen(memoViewModel: MemoViewModel, todoItem: TodoItemData, onBackEvent: () -> Unit) {
    var memoText by remember { mutableStateOf(todoItem.content) }
    var selectedTab by remember { mutableStateOf(MemoWriteTabMenu.KEYBOARD) }
    var showDatePicker by remember { mutableStateOf(false) }
    val tabs = listOf(MemoWriteTabMenu.KEYBOARD, MemoWriteTabMenu.HANDWRITING)
    var selectedDate by remember { mutableStateOf(todoItem.deadlineDate) }
    val memoUiState by memoViewModel.memoUiState.collectAsStateWithLifecycle()

    LaunchedEffect(memoUiState.lastSuccessfulAction) {
        if (memoUiState.lastSuccessfulAction == MemoViewModel.SUCCESSFUL_CREATE_MEMO || memoUiState.lastSuccessfulAction == MemoViewModel.SUCCESSFUL_UPDATE_MEMO) {
            memoViewModel.resetLastSuccessfulAction()
            onBackEvent()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (showDatePicker){
                DatePickerModal(DatePickerSelectableDates(false),{ selectedNewDate -> selectedDate = selectedNewDate  }, { it -> showDatePicker = it })
            }

            MemoWriteTopAppBar(
                if (todoItem.memoId.isBlank()) stringResource(R.string.write_memo_title) else stringResource(R.string.write_memo_title_update),
                onCloseClick = { onBackEvent() },
                onSaveClick = {
                    if (todoItem.memoId.isBlank()) memoViewModel.createMemo(todoItem.copy(content= memoText, deadlineDate = selectedDate))
                    else memoViewModel.updateMemo(todoItem.copy(content= memoText, deadlineDate = selectedDate))
                }
            )

            Column(
                modifier = Modifier.padding(JinadaDimens.Padding.medium),
                verticalArrangement = Arrangement.spacedBy(JinadaDimens.Spacer.large)
            ) {
                CommonCard(modifier = Modifier) {
                    Column {
                        ListItemRow(
                            icon = Icons.Default.LocationOn,
                            onClick = { /* 위치 선택 */ }
                        ) {
                            Text(text = stringResource(R.string.selected_location), style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.height(JinadaDimens.Spacer.xxxSmall))
                            Text(text = todoItem.locationName, style = MaterialTheme.typography.bodyLarge)
                        }

                        CommonDividingLine(modifier = Modifier.padding(horizontal = JinadaDimens.Padding.medium))

                        ListItemRow(
                            icon = Icons.Default.CalendarToday,
                            onClick = { showDatePicker = true }
                        ) {
                            Text(text = stringResource(R.string.setting_deadline), style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            Spacer(modifier = Modifier.height(JinadaDimens.Spacer.xxxSmall))
                            Text(text = selectedDate, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }

                CommonCard(modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(JinadaDimens.Padding.medium)) {
                        CommonTabRow(Modifier.commonTabRow(),selectedTab,tabs, onClickEvent = { newSelect -> selectedTab = newSelect }, tabTitleResId = { it.titleResId })

                        Spacer(modifier = Modifier.height(JinadaDimens.Spacer.xSmall))

                        OutlinedTextField(
                            value = memoText,
                            onValueChange = { memoText = it },
                            placeholder = { Text(stringResource(R.string.input_memo)) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }
        if (memoUiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoWriteTopAppBar(title : String,
    onCloseClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text(title, fontWeight = FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = onCloseClick) {
                Icon(Icons.Default.Close, contentDescription = stringResource(R.string.common_close))
            }
        },
        actions = {
            TextButton(onClick = onSaveClick) {
                Text(stringResource(R.string.button_save), fontWeight = FontWeight.Bold)
            }
        }
    )
}