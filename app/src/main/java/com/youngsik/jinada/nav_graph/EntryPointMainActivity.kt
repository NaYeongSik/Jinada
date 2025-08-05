package com.youngsik.jinada.nav_graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.youngsik.jinada.common.JinadaApplication
import com.youngsik.jinada.manager.ActivityRecognitionManagerImpl
import com.youngsik.jinada.presentation.screen.OnboardingScreen
import com.youngsik.jinada.presentation.screen.SplashScreen
import com.youngsik.jinada.presentation.viewmodel.SettingsViewModel
import kotlinx.coroutines.delay

@Composable
fun EntryPointMainActivity(){
    val context = LocalContext.current
    val navController = rememberNavController()
    val activityRecognitionManager = ActivityRecognitionManagerImpl(context)

    val appContainer = (context.applicationContext as JinadaApplication).container
    val viewModelFactory = appContainer.viewModelFactory
    val settingsViewModel: SettingsViewModel = viewModel(factory = viewModelFactory)

    NavHost(
        navController = navController,
        startDestination = ScreenRouteDef.Splash
    ){
        composable<ScreenRouteDef.Splash>{
            SplashScreen()

            LaunchedEffect(Unit){
                delay(800)

                navController.navigate(ScreenRouteDef.OnboardingScreen){
                    popUpTo(ScreenRouteDef.Splash){
                        inclusive = true
                    }
                }
            }
        }

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
            EntryPointMainScreen()
        }
    }
}