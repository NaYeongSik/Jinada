package com.youngsik.jinada.nav_graph

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.youngsik.jinada.data.dataclass.TodoItemData
import com.youngsik.jinada.data.remote.FirestoreMemoDataSourceImpl
import com.youngsik.jinada.data.repository.MemoRepositoryImpl
import com.youngsik.jinada.nav_type.parcelableType
import com.youngsik.jinada.presentation.factory.ViewModelFactory
import com.youngsik.jinada.presentation.screen.MainScreen
import com.youngsik.jinada.presentation.screen.MemoWriteScreen
import com.youngsik.jinada.presentation.screen.MyMemoScreen
import com.youngsik.jinada.presentation.screen.MyPageScreen
import com.youngsik.jinada.presentation.screen.StatisticsScreen
import com.youngsik.jinada.presentation.viewmodel.MemoMapViewModel
import com.youngsik.jinada.presentation.viewmodel.MemoViewModel
import kotlin.reflect.typeOf

@Preview(showBackground = true)
@Composable
fun EntryPointMainScreen(){
    val navController = rememberNavController()
    val memoDataSource = remember { FirestoreMemoDataSourceImpl() }
    val repository = remember { MemoRepositoryImpl(memoDataSource) }
    val factory = remember(repository) { ViewModelFactory(repository) }

    val memoViewModel: MemoViewModel = viewModel(factory = factory)
    val memoMapViewModel: MemoMapViewModel = viewModel(factory = factory)

    AppScaffold(navController = navController) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ScreenRouteDef.BottomNavigation.MainTab,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<ScreenRouteDef.BottomNavigation.MainTab>{
                MainScreen(memoMapViewModel,onCreateMemoClick = { todoItemData ->
                        navController.navigate(ScreenRouteDef.MemoManagementTab.CreateMemo(todoItemData))
                    },
                    onMemoUpdateClick = { todoItemData ->
                        navController.navigate(ScreenRouteDef.MemoManagementTab.UpdateMemo(todoItemData))
                    }
                )
            }
            composable<ScreenRouteDef.BottomNavigation.MyMemoTab>{
                MyMemoScreen(memoViewModel,onMemoUpdateClick= { todoItemData ->
                    navController.navigate(
                        ScreenRouteDef.MemoManagementTab.UpdateMemo(
                            todoItemData
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
            composable<ScreenRouteDef.MemoManagementTab.CreateMemo>(
                typeMap = mapOf( typeOf<TodoItemData>() to parcelableType<TodoItemData>())
            ){ navBackStackEntry ->
                val todoItem = navBackStackEntry.toRoute<ScreenRouteDef.MemoManagementTab.CreateMemo>().todoItem
                MemoWriteScreen(memoViewModel,todoItem= todoItem, onBackEvent = { navController.navigateUp() })
            }

            composable<ScreenRouteDef.MemoManagementTab.UpdateMemo>(
                typeMap = mapOf( typeOf<TodoItemData>() to parcelableType<TodoItemData>())
            ){ navBackStackEntry ->
                val todoItem = navBackStackEntry.toRoute<ScreenRouteDef.MemoManagementTab.UpdateMemo>().todoItem
                MemoWriteScreen(memoViewModel,todoItem= todoItem, onBackEvent = { navController.navigateUp() })
            }


        }
    }
}