package com.youngsik.jinada.presentation.composable

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.youngsik.jinada.presentation.R
import com.youngsik.jinada.shared.theme.JinadaDimens


@Composable
fun MapSearchBar(modifier: Modifier, inputText: String, onValueChange: (String)-> Unit){
    OutlinedTextField(
        modifier = modifier,
        value = inputText,
        onValueChange = { inputValue -> onValueChange(inputValue) },
        placeholder = { Text(text = stringResource(R.string.search_map_placeholder)) },
        leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = stringResource(R.string.search_icon)) },
        shape = RoundedCornerShape(JinadaDimens.Corner.small),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedBorderColor = Color.LightGray
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