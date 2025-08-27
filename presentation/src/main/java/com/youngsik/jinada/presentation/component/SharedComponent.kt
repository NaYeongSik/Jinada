package com.youngsik.jinada.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.youngsik.domain.entity.TodoItemData
import com.youngsik.shared.R
import com.youngsik.shared.theme.JinadaDimens

@Composable
fun CommonLazyColumnCard(modifier: Modifier, memoList: List<TodoItemData>, onCheckChange: (item: TodoItemData, isChecked: Boolean) -> Unit, onEditClick: (TodoItemData) -> Unit, onDeleteClick: (TodoItemData) -> Unit, onClick: ((TodoItemData) -> Unit)?=null ){
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(JinadaDimens.Spacer.small),
        contentPadding = PaddingValues(bottom = JinadaDimens.Padding.medium)
    ){
        items(
            items = memoList,
            key = { it.memoId }
        ) { item ->
            MemoCard(item,{ isChecked ->
                onCheckChange(item, isChecked)
            }
                ,{ onEditClick(item) }
                ,{ onDeleteClick(item) }
                ,{ onClick?.invoke(item) }
            )
        }
    }
}

@Composable
fun MemoCard(item: TodoItemData, onCheckBoxChange: (Boolean) -> Unit, onEditClick: () -> Unit, onDeleteClick: () -> Unit, onClick: (() -> Unit)?=null){
    Card (
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(JinadaDimens.Corner.medium),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(JinadaDimens.Common.xxxSmall, Color(0xFFFFCDD2)),
    ){
        var isExpanded by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier
                .padding(horizontal = JinadaDimens.Padding.xSmall, vertical = JinadaDimens.Padding.medium)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.isCompleted,
                onCheckedChange = onCheckBoxChange
            )

            Spacer(modifier = Modifier.width(JinadaDimens.Spacer.small))

            Column(modifier = Modifier.weight(1f).clickable{ onClick?.invoke() }) {
                Text(
                    text = stringResource(R.string.deadline_date,item.deadlineDate),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(JinadaDimens.Spacer.xSmall))
                Text(
                    text = item.content,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(JinadaDimens.Spacer.xSmall))
                if (item.distance != 0.0){
                    Text(
                        text = stringResource(R.string.address_distance_info,item.locationName,item.distance),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                else{
                    Text(
                        text = stringResource(R.string.address_no_distance_info,item.locationName),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            Box{
                IconButton(onClick = { isExpanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "더보기",
                        tint = Color.Gray
                    )
                }
                DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.button_edit)) },
                        onClick = {
                            isExpanded = false
                            onEditClick()
                        },
                        leadingIcon = { Icon(Icons.Default.Edit, "수정") }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.button_delete)) },
                        onClick = {
                            isExpanded = false
                            onDeleteClick()
                        },
                        leadingIcon = { Icon(Icons.Default.Delete, "삭제") }
                    )
                }
            }

        }
    }
}