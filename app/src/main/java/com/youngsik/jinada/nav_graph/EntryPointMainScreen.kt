package com.youngsik.jinada.nav_graph

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.youngsik.jinada.presentation.screen.MainScreen
import com.youngsik.jinada.presentation.screen.MemoWriteScreen
import com.youngsik.jinada.presentation.screen.MyMemoScreen
import com.youngsik.jinada.presentation.screen.MyPageScreen
import com.youngsik.jinada.presentation.screen.StatisticsScreen

@Preview(showBackground = true)
@Composable
fun EntryPointMainScreen(){
    val navController = rememberNavController()

    AppScaffold(navController = navController) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ScreenRouteDef.BottomNavigation.MainTab,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<ScreenRouteDef.BottomNavigation.MainTab>{
                MainScreen(
                    onCreateMemoClick = { address ->
                        navController.navigate(ScreenRouteDef.MemoManagementTab.CreateMemo(address))
                    },
                    onMemoUpdateClick = { memoId ->
                        navController.navigate(ScreenRouteDef.MemoManagementTab.UpdateMemo(memoId))
                    }
                )
            }
            composable<ScreenRouteDef.BottomNavigation.MyMemoTab>{
                MyMemoScreen({ memoId ->
                    navController.navigate(
                        ScreenRouteDef.MemoManagementTab.UpdateMemo(
                            memoId
                        )
                    )
                })
            }
            composable<ScreenRouteDef.BottomNavigation.MyPageTab>{
                MyPageScreen()
            }
            composable<ScreenRouteDef.BottomNavigation.StatisticsTab>{
                StatisticsScreen()
            }
            composable<ScreenRouteDef.MemoManagementTab.CreateMemo>{ navBackStackEntry ->
                val selectedAddress = navBackStackEntry.toRoute<ScreenRouteDef.MemoManagementTab.CreateMemo>().address
                MemoWriteScreen(address = selectedAddress, memoId = null, onBackEvent = { navController.navigateUp() })
            }

            composable<ScreenRouteDef.MemoManagementTab.UpdateMemo>{ navBackStackEntry ->
                val selectedMemoId = navBackStackEntry.toRoute<ScreenRouteDef.MemoManagementTab.UpdateMemo>().memoId
                MemoWriteScreen(memoId = selectedMemoId, address = null, onBackEvent = { navController.navigateUp() })
            }


        }
    }
}