package com.youngsik.jinada.nav_graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.youngsik.jinada.presentation.common.SharedPreferenceManager
import com.youngsik.jinada.presentation.screen.OnboardingScreen
import com.youngsik.jinada.presentation.screen.SplashScreen
import kotlinx.coroutines.delay

@Composable
fun EntryPointMainActivity(){
    val navController = rememberNavController()
    val sharedPreferenceManager = SharedPreferenceManager(LocalContext.current)

    NavHost(
        navController = navController,
        startDestination = ScreenRouteDef.Splash
    ){
        composable<ScreenRouteDef.Splash>{
            SplashScreen()

            LaunchedEffect(Unit){
                val route = if (sharedPreferenceManager.getNickname() != null) ScreenRouteDef.Entryscreen else ScreenRouteDef.OnboardingScreen
                delay(800)

                navController.navigate(route){
                    popUpTo(ScreenRouteDef.Splash){
                        inclusive = true
                    }
                }
            }
        }

        composable<ScreenRouteDef.OnboardingScreen>{
            OnboardingScreen{ nickname ->
                sharedPreferenceManager.setNickname(nickname)

                navController.navigate(ScreenRouteDef.Entryscreen){
                popUpTo(ScreenRouteDef.Splash){
                    inclusive = true
                }
            } }
        }

        composable<ScreenRouteDef.Entryscreen>{
            EntryPointMainScreen()
        }
    }
}