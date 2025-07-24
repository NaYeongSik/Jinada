package com.youngsik.jinada.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youngsik.jinada.data.dataclass.TodoItemData
import com.youngsik.jinada.data.utils.changeToStringDate
import com.youngsik.jinada.data.utils.getCompleteRateData
import com.youngsik.jinada.presentation.R
import com.youngsik.jinada.presentation.common.DatePickerModal
import com.youngsik.jinada.presentation.common.DatePickerSelectableDates
import com.youngsik.jinada.presentation.component.CommonLazyColumnCard
import com.youngsik.jinada.presentation.theme.JinadaDimens
import com.youngsik.jinada.presentation.viewmodel.MemoViewModel
import java.time.LocalDate

@Composable
fun MyMemoScreen(memoViewModel: MemoViewModel, onMemoUpdateClick: (TodoItemData)-> Unit){
    var showDatePicker by remember { mutableStateOf(false) }
    val memoUiState by memoViewModel.memoUiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Column (
            modifier = Modifier.fillMaxSize()
        ){
            if (showDatePicker){
                DatePickerModal(DatePickerSelectableDates(true),{ selectedNewDate -> memoViewModel.changeSelectedDate(selectedNewDate) }, { it -> showDatePicker = it })
            }

            DateSelectSection(memoUiState.selectedDate,{ showDatePicker = true })

            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                ProgressBarSection(memoUiState.memoList)

                Spacer(modifier = Modifier.padding(JinadaDimens.Padding.xxSmall))

                CommonLazyColumnCard(modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(0.9f), memoList = memoUiState.memoList, onCheckChange = {
                        item, isChecked -> // TODO: 해당 메모 데이터 완료 처리 필요
                    val index = memoUiState.memoList.indexOf(item)
                    if (index != -1) {
                        memoViewModel.updateMemo(item.copy(isCompleted = isChecked, completeDate = if (isChecked) changeToStringDate(LocalDate.now()) else null))
                    }
                }, { item -> onMemoUpdateClick(item) }, { item -> memoViewModel.deleteMemo(item.memoId) })

            }
        }
        if (memoUiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun DateSelectSection(selectedDate: String, onClickEvent: ()-> Unit){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(JinadaDimens.Padding.medium)
            .clickable(onClick = onClickEvent),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        Text(
            text = selectedDate,
            style = MaterialTheme.typography.bodyLarge
        )

        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = stringResource(R.string.select_date)
        )
    }
}

@Composable
fun ProgressBarSection(memoList: List<TodoItemData>){
    val completeRateData = getCompleteRateData(memoList)
    Column(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .padding(JinadaDimens.Padding.xxxSmall),
        verticalArrangement = Arrangement.spacedBy(JinadaDimens.Spacer.xxSmall)
    ) {
        Text(text = stringResource(R.string.memo_progress_rate),
            modifier = Modifier.align(Alignment.Start),
            style = MaterialTheme.typography.bodyMedium)

        LinearProgressIndicator(
            progress = { completeRateData.rate },
            modifier = Modifier
                .fillMaxWidth()
                .height(JinadaDimens.Common.small)
                .clip(CircleShape),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )

        Text(text = "${completeRateData.successCount}/${completeRateData.totalCount}  (${(completeRateData.rate * 100).toInt()}%)",
            modifier = Modifier.align(Alignment.End), style = MaterialTheme.typography.bodyMedium)
    }
}
