package com.youngsik.jinada

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.youngsik.jinada.ui.screen.EntryPointMainScreen
import com.youngsik.jinada.shared.theme.JinadaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JinadaTheme {
                EntryPointMainScreen()
            }
        }
    }
}