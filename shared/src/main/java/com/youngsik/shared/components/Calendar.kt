package com.youngsik.shared.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import com.youngsik.shared.common.DatePickerSelectableDates
import com.youngsik.shared.utils.changeToStringDate
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(selectableDates : DatePickerSelectableDates, onDateSelected: (String) -> Unit, onDismiss: (Boolean) -> Unit) {
    val datePickerState = rememberDatePickerState(selectableDates = selectableDates)

    DatePickerDialog(
        onDismissRequest = { onDismiss(false) },
        confirmButton = {
            TextButton(onClick = {
                val selectedMillis = datePickerState.selectedDateMillis
                if (selectedMillis != null) {
                    val newDate = Instant.ofEpochMilli(selectedMillis)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    onDateSelected(changeToStringDate(newDate))
                }
                onDismiss(false)
            }) {
                Text("확인")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss(false) }) {
                Text("취소")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}