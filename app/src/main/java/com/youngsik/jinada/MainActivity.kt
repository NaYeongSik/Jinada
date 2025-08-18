package com.youngsik.jinada

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.youngsik.jinada.common.JinadaApplication
import com.youngsik.jinada.presentation.navigation.graph.EntryMain
import com.youngsik.shared.theme.JinadaTheme

class MainActivity : ComponentActivity() {
    private val appContainer by lazy {
        (applicationContext as JinadaApplication).container
    }
    private val viewModelFactory by lazy { appContainer.viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JinadaTheme {
                EntryMain(viewModelFactory)
            }
        }
    }
}