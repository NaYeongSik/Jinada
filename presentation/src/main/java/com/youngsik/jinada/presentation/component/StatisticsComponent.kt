package com.youngsik.jinada.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.youngsik.domain.model.CompleteRateData
import com.youngsik.domain.model.StatisticsData
import com.youngsik.domain.model.TodoItemData
import com.youngsik.jinada.presentation.R
import com.youngsik.jinada.presentation.common.StatTabMenu
import com.youngsik.jinada.presentation.theme.JinadaDimens

@Composable
fun MainStatisticsSection(selectedTab: StatTabMenu, statData: StatisticsData, completeRateData: CompleteRateData, onChangeTab: (StatTabMenu)-> Unit){
    val tabs = listOf(StatTabMenu.TOTALLY, StatTabMenu.MONTHLY, StatTabMenu.WEEKLY)

    CommonCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = JinadaDimens.Padding.large)
    ) {
        Column(
            modifier = Modifier.padding(JinadaDimens.Padding.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CommonTabRow(
                Modifier
                .commonTabRow()
                .height(JinadaDimens.Common.medium),
                selectedTab,
                tabs,
                onClickEvent = { newSelect ->
                    onChangeTab(newSelect)
                },
                tabTitleResId = { it.titleResId })
            Spacer(modifier = Modifier.height(JinadaDimens.Spacer.medium))
            MainProgressSection(completeRateData, statData, selectedTab)
        }
    }
}

@Composable
fun MainProgressSection(completeRateData: CompleteRateData,statData: StatisticsData,selectedTab: StatTabMenu) {
    Column(
        modifier = Modifier.padding(JinadaDimens.Padding.large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(JinadaDimens.Spacer.xLarge)
    ) {
        CircularProgressWithLabel(completeRateData)
        TextSummarySection(statData,selectedTab)
    }
}

@Composable
fun MemoCountCardSection(completeRateData: CompleteRateData){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(JinadaDimens.Spacer.medium)
    ) {
        SummaryCard(title = stringResource(R.string.summary_card_title_total_memos), count = completeRateData.totalCount, modifier = Modifier.weight(1f))
        SummaryCard(title = stringResource(R.string.summary_card_title_completed_memos), count = completeRateData.successCount, modifier = Modifier.weight(1f), highlight = true)
    }
}

@Composable
fun SummaryCard(title: String, count: Int, modifier: Modifier = Modifier, highlight: Boolean = false) {
    CommonCard(modifier = modifier) {
        Column(modifier = Modifier.padding(JinadaDimens.Padding.medium)) {
            Text(text = title, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Spacer(modifier = Modifier.height(JinadaDimens.Spacer.xSmall))
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = if (highlight) MaterialTheme.colorScheme.primary else Color.Black
            )
        }
    }
}

@Composable
fun CircularProgressWithLabel(completeRateData: CompleteRateData) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(JinadaDimens.Common.xLarge)) {
        CircularProgressIndicator(
            progress = { 1f },
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFEEEEEE),
            strokeWidth = JinadaDimens.Common.xSmall,
            strokeCap = StrokeCap.Round
        )
        CircularProgressIndicator(
            progress = { completeRateData.rate },
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = JinadaDimens.Common.xSmall,
            strokeCap = StrokeCap.Round
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "${(completeRateData.rate * 100).toInt()}%", style = MaterialTheme.typography.titleLarge)
            Text(text = stringResource(R.string.progress_label_completion_rate), style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
fun IncompleteTodosSection(showIncompletedMemo: Boolean,onClick: ()-> Unit) {
    CommonCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(JinadaDimens.Common.large)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(JinadaDimens.Padding.medium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.incomplete_memos_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = onClick) {
                if (showIncompletedMemo) Text(stringResource(R.string.common_close)) else Text(
                    stringResource(R.string.common_show_all)
                )
            }
        }
    }
}

@Composable
fun TextSummarySection(statData: StatisticsData, selectedTab: StatTabMenu) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(JinadaDimens.Common.xLarge)
    ) {
        when (selectedTab) {
            StatTabMenu.TOTALLY -> {
                if (statData is StatisticsData.TotallyStatData) {
                    TotalySummary(statData)
                }
            }
            StatTabMenu.MONTHLY -> {
                if (statData is StatisticsData.MonthlyStatData) {
                    MonthlySummary(statData)
                }
            }
            StatTabMenu.WEEKLY -> {
                if (statData is StatisticsData.WeeklyStatData) {
                    WeeklySummary(statData)
                }
            }
        }
    }
}

@Composable
fun WeeklySummary(weeklyStat: StatisticsData.WeeklyStatData){

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(JinadaDimens.Spacer.xSmall)
    ){
        Text(text = stringResource(R.string.analysis_title_weekly), style = MaterialTheme.typography.bodyLarge)
        if (weeklyStat.mostCompletedDayOfWeek.isNotBlank()) Text(text = stringResource(R.string.analysis_weekly_most_completed_day,weeklyStat.mostCompletedDayOfWeek,weeklyStat.mostCompletedCount))

        if (weeklyStat.incompletedCount != 0)Text(text = stringResource(R.string.analysis_weekly_upcoming_memos,weeklyStat.incompletedCount))
        if (weeklyStat.mostCompletedCount == 0 && weeklyStat.incompletedCount == 0) Text(text = stringResource(R.string.analysis_weekly_not_have_memos))
        else Text(text = stringResource(R.string.analysis_weekly_all_memos_completed))
    }
}

@Composable
fun MonthlySummary(montlyStat: StatisticsData.MonthlyStatData){

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(JinadaDimens.Spacer.xSmall)
    ){
        Text(text = stringResource(R.string.analysis_title_monthly), style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun TotalySummary(totalyStat: StatisticsData.TotallyStatData){

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(JinadaDimens.Spacer.xSmall)
    ){
        Text(text = stringResource(R.string.analysis_title_total), style = MaterialTheme.typography.bodyLarge)
        if (totalyStat.totalCompletedMemoCount != 0) Text(text = stringResource(R.string.analysis_total_completed_memos,totalyStat.totalCompletedMemoCount))
        else Text(text = stringResource(R.string.analysis_common_no_completed_memos))

        if (totalyStat.bestMonth.isNotBlank())Text(text = stringResource(R.string.analysis_total_best_month,totalyStat.bestMonth,totalyStat.bestMonthCompletedCount))
        else Text(text = stringResource(R.string.analysis_common_no_completed_memos))
    }
}
