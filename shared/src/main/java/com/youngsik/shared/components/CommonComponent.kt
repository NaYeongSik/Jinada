package com.youngsik.shared.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import com.youngsik.shared.R
import com.youngsik.shared.theme.JinadaDimens


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
                    ) { Text(stringResource(R.string.button_cancle)) }
                    Button(onClick = onSave, modifier = Modifier.weight(1f)) { Text(stringResource(R.string.button_save)) }
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
