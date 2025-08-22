package com.youngsik.jinada.presentation.navigation.graph

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.youngsik.jinada.presentation.navigation.route.ScreenRouteDef
import com.youngsik.jinada.presentation.screen.OnboardingScreen
import com.youngsik.jinada.presentation.viewmodel.SettingsViewModel

@Composable
fun EntryMain(){
    val navController = rememberNavController()
    val settingsViewModel: SettingsViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = ScreenRouteDef.OnboardingScreen
    ){

        composable<ScreenRouteDef.OnboardingScreen>{
            OnboardingScreen(settingsViewModel){
                navController.navigate(ScreenRouteDef.Entryscreen){
                    popUpTo(ScreenRouteDef.OnboardingScreen){
                        inclusive = true
                    }
                }
            }
        }

        composable<ScreenRouteDef.Entryscreen>{
            EntryPointMainScreen()
        }
    }
}