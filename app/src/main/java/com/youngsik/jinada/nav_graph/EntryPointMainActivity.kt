package com.youngsik.jinada.nav_graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.youngsik.jinada.ui.screen.EntryPointMainScreen
import com.youngsik.jinada.ui.screen.SplashScreen
import kotlinx.coroutines.delay

@Composable
fun EntryPointMainActivity(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ScreenRouteDef.Splash
    ){
        composable<ScreenRouteDef.Splash>{
            SplashScreen()

            LaunchedEffect(Unit){
                delay(2000)
                navController.navigate(ScreenRouteDef.entryscreen){
                    popUpTo(ScreenRouteDef.Splash){
                        inclusive = true
                    }
                }
            }
        }
        composable<ScreenRouteDef.entryscreen>{
            EntryPointMainScreen()
        }
    }
}