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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import com.youngsik.jinada.data.TodoItemData
import com.youngsik.jinada.presentation.theme.JinadaDimens


@Composable
fun CommonDividingLine(modifier: Modifier){
    HorizontalDivider(
        modifier = modifier,
        thickness = JinadaDimens.Common.xxxSmall,
        color = Color(0xFFF0F0F0)
    )
}

fun Modifier.commonTabRow() = this.fillMaxWidth().clip(CircleShape)

@Composable
fun CommonCard(modifier: Modifier,content: @Composable () -> Unit){
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(JinadaDimens.Corner.medium),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(JinadaDimens.Common.xxxSmall, Color(0xFFFFCDD2)),
    ) {
        content()
    }
}

@Composable
fun CommonLazyColumnCard(modifier: Modifier, memoList: List<TodoItemData>, onCheckChange: (item: TodoItemData, isChecked: Boolean) -> Unit, onEditClick: (TodoItemData) -> Unit, onDeleteClick: (TodoItemData) -> Unit ){
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(JinadaDimens.Spacer.small),
        contentPadding = PaddingValues(bottom = JinadaDimens.Padding.medium)
    ){
        items(
            items = memoList,
            key = { it.id }
        ) { item ->
            MemoCard(item,{ isChecked ->
                    onCheckChange(item, isChecked)
                }
                ,{ onEditClick(item) }
                ,{ onDeleteClick(item) }
            )
        }
    }
}

@Composable
fun MemoCard(item: TodoItemData, onCheckBoxChange: (Boolean) -> Unit, onEditClick: () -> Unit, onDeleteClick: () -> Unit){
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

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "마감일: ${ item.deadlineDate }",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(JinadaDimens.Spacer.xSmall))
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(JinadaDimens.Spacer.xSmall))
                Text(
                    text = "${item.storeInfo} / ${item.distance}",
                    style = MaterialTheme.typography.labelMedium
                )
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
                        text = { Text("수정") },
                        onClick = {
                            isExpanded = false
                            onEditClick()
                        },
                        leadingIcon = { Icon(Icons.Default.Edit, "수정") }
                    )
                    DropdownMenuItem(
                        text = { Text("삭제") },
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

@Composable
fun ListItemRow(icon: ImageVector?, onClick: () -> Unit, content: @Composable () -> Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = JinadaDimens.Padding.medium,vertical = JinadaDimens.Padding.xSmall),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (icon != null){
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(JinadaDimens.Spacer.medium))
        }

        Column(modifier = Modifier.weight(1f)) {
            content()
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "이동",
            tint = Color.Gray
        )
    }
}

@Composable
fun <T> CommonTabRow(modifier: Modifier = Modifier, selectedTab: T, tabs: List<T>, onClickEvent: (T) -> Unit, tabTitleResId: (T) -> Int) {
    Surface(
        shape = CircleShape,
        color = Color(0xFFEEEEEE),
        modifier = modifier
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            tabs.forEach { tabName ->
                Button(
                    onClick = { onClickEvent(tabName) },
                    shape = CircleShape,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == tabName) MaterialTheme.colorScheme.primary else Color.Transparent,
                        contentColor = if (selectedTab == tabName) Color.White else Color.Gray
                    ),
                    elevation = ButtonDefaults.buttonElevation(JinadaDimens.Common.none, JinadaDimens.Common.none)
                ) {
                    Text(text = stringResource(tabTitleResId(tabName)), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CommonSettingsDialog(title: String, onDismiss: () -> Unit, onSave: () -> Unit, content: @Composable () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        CommonCard(modifier = Modifier.fillMaxWidth(0.95f)) {
            Column(
                modifier = Modifier.padding(JinadaDimens.Padding.large)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(JinadaDimens.Spacer.xSmall))
                CommonDividingLine(modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(JinadaDimens.Spacer.xSmall))

                content()

                Spacer(Modifier.height(JinadaDimens.Spacer.large))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(JinadaDimens.Spacer.xSmall)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) { Text("취소") }
                    Button(onClick = onSave, modifier = Modifier.weight(1f)) { Text("저장") }
                }
            }
        }
    }
}

@Composable
fun CommonSwitchOptionRow(isChecked: Boolean, onCheckChange: (Boolean) -> Unit,content: @Composable () -> Unit){
    Row (
        modifier = Modifier.fillMaxWidth().padding(vertical = JinadaDimens.Padding.xxSmall),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Column (
            modifier = Modifier
        ){
            content()
        }
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckChange
        )
    }
}

@Composable
fun CommonSliderOptionRow(value: Float, onValueChanged: (Float)-> Unit, content: @Composable () -> Unit){
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            content()
        }
        Slider(value = value,
            onValueChange = onValueChanged,
            valueRange = 0.1f..0.5f)
    }
}
