package com.youngsik.jinada.presentation.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.youngsik.shared.theme.JinadaDimens
import com.youngsik.shared.R


@Composable
fun MapSearchBar(modifier: Modifier, inputText: String, onValueChange: (String)-> Unit, onSearch: () -> Unit){
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        modifier = modifier,
        value = inputText,
        onValueChange = { inputValue -> onValueChange(inputValue) },
        placeholder = { Text(text = stringResource(R.string.search_map_placeholder),style = MaterialTheme.typography.bodyMedium) },
        leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = stringResource(R.string.search_icon)) },
        shape = RoundedCornerShape(JinadaDimens.Corner.small),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedBorderColor = Color.LightGray
        ),
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch()
                keyboardController?.hide()
            }
        )
    )
}

@Composable
fun MyLocationButton(modifier: Modifier,onClickEvent:()-> Unit){
    FloatingActionButton(
        onClick = onClickEvent,
        modifier = modifier,
        containerColor = Color.White,
        contentColor = Color.Black
    ) {
        Icon(Icons.Default.MyLocation, contentDescription = stringResource(R.string.current_location_icon))
    }
}