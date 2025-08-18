package com.youngsik.jinada.presentation.navigation.route

import com.youngsik.jinada.presentation.data.TodoItemParcelable
import kotlinx.serialization.Serializable

@Serializable
sealed interface ScreenRouteDef {

    @Serializable
    data object Splash : ScreenRouteDef

    @Serializable
    data object OnboardingScreen : ScreenRouteDef

    @Serializable
    data object Entryscreen : ScreenRouteDef

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
        data class CreateMemo(val todoItem: TodoItemParcelable): MemoManagementTab

        @Serializable
        data class UpdateMemo(val todoItem: TodoItemParcelable) : MemoManagementTab
    }

}