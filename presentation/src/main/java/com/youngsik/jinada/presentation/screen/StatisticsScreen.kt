package com.youngsik.jinada.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.youngsik.jinada.presentation.composable.IncompleteTodosSection
import com.youngsik.jinada.presentation.composable.MainStatisticsSection
import com.youngsik.jinada.presentation.composable.MemoCountCardSection
import com.youngsik.jinada.presentation.MemoMockData
import com.youngsik.jinada.presentation.common.MemoCard
import com.youngsik.jinada.presentation.common.StatTabMenu
import com.youngsik.jinada.shared.theme.JinadaDimens
import com.youngsik.jinada.shared.utils.changeToStringDate
import com.youngsik.jinada.shared.utils.getCompleteRateData
import java.time.LocalDate

@Preview(showBackground = true)
@Composable
fun StatisticsScreen(){
    var showIncompletedMemo by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(StatTabMenu.WEEKLY) }
    val memoList = remember { mutableStateListOf(*MemoMockData.getCompleteRateDataInWeek().toTypedArray()) }
    val completeRateData = getCompleteRateData(memoList)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF5F0))
            .padding(JinadaDimens.Padding.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(JinadaDimens.Spacer.large)
    ) {
        item {
            MainStatisticsSection(selectedTab,memoList,completeRateData
                ,{ newTab-> selectedTab = newTab }
                , { newMemoList ->
                memoList.clear()
                memoList.addAll(newMemoList)})
        }

        item {
            MemoCountCardSection(completeRateData)
        }

        item {
            IncompleteTodosSection(showIncompletedMemo,{ showIncompletedMemo = !showIncompletedMemo})
        }


        if(showIncompletedMemo){
            items(
                items = memoList.filter { it -> !it.isCompleted },
                key = { it.id }
            ) { item ->
                MemoCard(item,{ isChecked ->
                    val index = memoList.indexOf(item)
                    if (index != -1) { memoList[index] = item.copy(isCompleted = isChecked, completeDate = changeToStringDate(
                        LocalDate.now())
                    ) }
                    // TODO: 해당 메모 데이터 완료 처리 필요
                }
                    ,{ /*TODO: 메모 생성화면으로 해당 데이터 가지고 이동 */ }
                    ,{ memoList.remove(item) /* TODO: 해당 메모 데이터 삭제 필요*/ }
                )
            }
        }

    }
}



