package com.youngsik.jinada.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.youngsik.shared.theme.JinadaDimens
import com.youngsik.shared.R

@Composable
fun SplashScreen(){
    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ){
        Image(
            painter = painterResource(R.drawable.jinada_logo),
            contentDescription = "",
            modifier = Modifier.size(JinadaDimens.Common.xxxLarge)
        )
    }
}