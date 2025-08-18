package com.youngsik.jinada.presentation.navigation.graph

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.youngsik.domain.manager.LocationServiceManager
import com.youngsik.jinada.presentation.data.TodoItemParcelable
import com.youngsik.jinada.presentation.data.toDomainModel
import com.youngsik.jinada.presentation.data.toParcelable
import com.youngsik.jinada.presentation.manager.LocationServiceManagerImpl
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
fun EntryPointMainScreen(context: Context,viewModelFactory: ViewModelProvider.Factory){
    val navController = rememberNavController()
    val locationServiceManager: LocationServiceManager = remember { LocationServiceManagerImpl(context) }

    val memoViewModel: MemoViewModel = viewModel(factory = viewModelFactory)
    val memoMapViewModel: MemoMapViewModel = viewModel(factory = viewModelFactory)
    val settingsViewModel: SettingsViewModel = viewModel(factory = viewModelFactory)

    AppScaffold(navController = navController) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ScreenRouteDef.BottomNavigation.MainTab,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<ScreenRouteDef.BottomNavigation.MainTab>{
                MainScreen(memoMapViewModel,locationServiceManager,onCreateMemoClick = { todoItemData ->
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