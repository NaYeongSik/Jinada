package com.youngsik.jinada

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.youngsik.jinada.presentation.navigation.graph.EntryMain
import com.youngsik.jinada.presentation.navigation.graph.EntryPointMainScreen
import com.youngsik.shared.theme.JinadaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JinadaTheme {
                EntryMain()
            }
        }
    }

   override fun onNewIntent(intent: Intent, caller: ComponentCaller) {
       super.onNewIntent(intent, caller)
       setContent {
           JinadaTheme {
               EntryPointMainScreen()
           }
       }
    }
}