package com.youngsik.jinada.presentation.navigation.graph

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.youngsik.jinada.presentation.data.TodoItemParcelable
import com.youngsik.jinada.presentation.data.toDomainModel
import com.youngsik.jinada.presentation.data.toParcelable
import com.youngsik.jinada.presentation.navigation.route.ScreenRouteDef
import com.youngsik.jinada.presentation.navigation.type.parcelableType
import com.youngsik.jinada.presentation.screen.MainScreen
import com.youngsik.jinada.presentation.screen.MemoWriteScreen
import com.youngsik.jinada.presentation.screen.MyMemoScreen
import com.youngsik.jinada.presentation.screen.MyPageScreen
import com.youngsik.jinada.presentation.screen.StatisticsScreen
import com.youngsik.jinada.presentation.viewmodel.MemoMapViewModel
import com.youngsik.jinada.presentation.viewmodel.MemoViewModel
import com.youngsik.jinada.presentation.viewmodel.SettingsViewModel
import kotlin.reflect.typeOf

@Composable
fun EntryPointMainScreen(){
    val navController = rememberNavController()

    val memoViewModel: MemoViewModel = hiltViewModel()
    val memoMapViewModel: MemoMapViewModel = hiltViewModel()
    val settingsViewModel: SettingsViewModel = hiltViewModel()

    AppScaffold(navController = navController) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ScreenRouteDef.BottomNavigation.MainTab,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<ScreenRouteDef.BottomNavigation.MainTab>{
                MainScreen(memoMapViewModel,onCreateMemoClick = { todoItemData ->
                        val todoItemParcelable = todoItemData.toParcelable()
                        navController.navigate(ScreenRouteDef.MemoManagementTab.CreateMemo(todoItemParcelable))
                    },
                    onMemoUpdateClick = { todoItemData ->
                        val todoItemParcelable = todoItemData.toParcelable()
                        navController.navigate(ScreenRouteDef.MemoManagementTab.UpdateMemo(todoItemParcelable))
                    }
                )
            }
            composable<ScreenRouteDef.BottomNavigation.MyMemoTab>{
                MyMemoScreen(memoViewModel){ todoItemData ->
                        val todoItemParcelable = todoItemData.toParcelable()
                        navController.navigate(ScreenRouteDef.MemoManagementTab.UpdateMemo(todoItemParcelable))
                }
            }
            composable<ScreenRouteDef.BottomNavigation.MyPageTab>{
                MyPageScreen(settingsViewModel)
            }
            composable<ScreenRouteDef.BottomNavigation.StatisticsTab>{
                StatisticsScreen(memoViewModel){ todoItemData ->
                    val todoItemParcelable = todoItemData.toParcelable()
                    navController.navigate(ScreenRouteDef.MemoManagementTab.UpdateMemo(todoItemParcelable))
                }
            }
            composable<ScreenRouteDef.MemoManagementTab.CreateMemo>(
                typeMap = mapOf( typeOf<TodoItemParcelable>() to parcelableType<TodoItemParcelable>())
            ){ navBackStackEntry ->
                val todoItemParcelable = navBackStackEntry.toRoute<ScreenRouteDef.MemoManagementTab.CreateMemo>().todoItem
                MemoWriteScreen(memoViewModel,todoItem= todoItemParcelable.toDomainModel(), onBackEvent = { navController.navigateUp() })
            }

            composable<ScreenRouteDef.MemoManagementTab.UpdateMemo>(
                typeMap = mapOf( typeOf<TodoItemParcelable>() to parcelableType<TodoItemParcelable>())
            ){ navBackStackEntry ->
                val todoItemParcelable = navBackStackEntry.toRoute<ScreenRouteDef.MemoManagementTab.UpdateMemo>().todoItem
                MemoWriteScreen(memoViewModel,todoItem= todoItemParcelable.toDomainModel(), onBackEvent = { navController.navigateUp() })
            }


        }
    }
}