package com.youngsik.jinada.nav_graph

import com.youngsik.jinada.data.dataclass.TodoItemData
import kotlinx.serialization.Serializable

@Serializable
sealed interface ScreenRouteDef {

    @Serializable
    data object Splash : ScreenRouteDef

    @Serializable
    data object entryscreen : ScreenRouteDef

    @Serializable
    sealed interface BottomNavigation : ScreenRouteDef {
        @Serializable
        data object MainTab: BottomNavigation

        @Serializable
        data object MyMemoTab: BottomNavigation

        @Serializable
        data object MyPageTab: BottomNavigation

        @Serializable
        data object StatisticsTab: BottomNavigation
    }

    @Serializable
    sealed interface MemoManagementTab : ScreenRouteDef{
        @Serializable
        data class CreateMemo(val todoItem: TodoItemData): MemoManagementTab

        @Serializable
        data class UpdateMemo(val todoItem: TodoItemData) : MemoManagementTab
    }

}