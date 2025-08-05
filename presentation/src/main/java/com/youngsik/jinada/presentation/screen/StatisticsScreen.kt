package com.youngsik.jinada.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youngsik.domain.model.TodoItemData
import com.youngsik.jinada.data.utils.changeToStringDate
import com.youngsik.jinada.presentation.component.IncompleteTodosSection
import com.youngsik.jinada.presentation.component.MainStatisticsSection
import com.youngsik.jinada.presentation.component.MemoCard
import com.youngsik.jinada.presentation.component.MemoCountCardSection
import com.youngsik.jinada.presentation.theme.JinadaDimens
import com.youngsik.jinada.presentation.viewmodel.MemoViewModel
import com.youngsik.jinada.presentation.viewmodel.MemoViewModel.Companion.SUCCESSFUL_DELETE_MEMO
import com.youngsik.jinada.presentation.viewmodel.MemoViewModel.Companion.SUCCESSFUL_GET_STATISTICS
import com.youngsik.jinada.presentation.viewmodel.MemoViewModel.Companion.SUCCESSFUL_UPDATE_MEMO
import java.time.LocalDate

@Composable
fun StatisticsScreen(memoViewModel: MemoViewModel,onMemoUpdateClick: (TodoItemData)-> Unit){
    var showIncompletedMemo by remember { mutableStateOf(false) }
    val memoUiState by memoViewModel.memoUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        memoViewModel.getMemoListBySelectedStatTabMenu(memoUiState.selectedTabMenu.name)
    }
    LaunchedEffect(memoUiState.lastSuccessfulAction) {
        if (memoUiState.lastSuccessfulAction == SUCCESSFUL_UPDATE_MEMO || memoUiState.lastSuccessfulAction == SUCCESSFUL_DELETE_MEMO) memoViewModel.getMemoListBySelectedStatTabMenu(memoUiState.selectedTabMenu.name)
    }

    Box (
        modifier = Modifier.fillMaxSize()
    ){
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF5F0))
                .padding(JinadaDimens.Padding.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(JinadaDimens.Spacer.large)
        ) {
            item {
                MainStatisticsSection(memoUiState.selectedTabMenu,memoUiState.statData,memoUiState.completeRateData
                    ,{ selectedTab-> if (memoUiState.selectedTabMenu != selectedTab && !memoUiState.isLoading) memoViewModel.changeSelectedTab(selectedTab) }
                )
            }

            item {
                MemoCountCardSection(memoUiState.completeRateData)
            }

            item {
                IncompleteTodosSection(showIncompletedMemo,{ showIncompletedMemo = !showIncompletedMemo})
            }


            if(showIncompletedMemo){
                items(
                    items = memoUiState.memoListInSelectedTab.filter { it -> !it.isCompleted },
                    key = { it.memoId }
                ) { item ->
                    MemoCard(item,{ isChecked ->
                        val index = memoUiState.memoListInSelectedTab.indexOf(item)
                        if (index != -1) {
                            memoViewModel.updateMemo(item.copy(isCompleted = isChecked, completeDate = if (isChecked) changeToStringDate(LocalDate.now()) else null))
                        }
                    }
                        ,{ onMemoUpdateClick(item) }
                        ,{ memoViewModel.deleteMemo(item.memoId) }
                    )
                }
            }
        }
        if (memoUiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }

}



