package com.youngsik.jinada.presentation.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.youngsik.jinada.presentation.manager.ActivityRecognitionManagerImpl
import com.youngsik.jinada.presentation.navigation.route.ScreenRouteDef
import com.youngsik.jinada.presentation.screen.OnboardingScreen
import com.youngsik.jinada.presentation.viewmodel.SettingsViewModel

@Composable
fun EntryMain(viewModelFactory: ViewModelProvider.Factory){
    val context = LocalContext.current
    val navController = rememberNavController()
    val activityRecognitionManager = ActivityRecognitionManagerImpl(context.applicationContext)

    val settingsViewModel: SettingsViewModel = viewModel(factory = viewModelFactory)

    NavHost(
        navController = navController,
        startDestination = ScreenRouteDef.OnboardingScreen
    ){

        composable<ScreenRouteDef.OnboardingScreen>{
            OnboardingScreen(settingsViewModel,activityRecognitionManager){
                navController.navigate(ScreenRouteDef.Entryscreen){
                    popUpTo(ScreenRouteDef.OnboardingScreen){
                        inclusive = true
                    }
                }
            }
        }

        composable<ScreenRouteDef.Entryscreen>{
            EntryPointMainScreen(context,viewModelFactory)
        }
    }
}