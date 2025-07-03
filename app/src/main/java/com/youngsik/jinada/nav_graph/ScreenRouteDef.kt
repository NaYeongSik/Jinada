package com.youngsik.jinada.nav_graph

import kotlinx.serialization.Serializable

@Serializable
sealed interface ScreenRouteDef {
    @Serializable
    data object MainTab: ScreenRouteDef

    @Serializable
    data object MyMemoTab: ScreenRouteDef

    @Serializable
    data object MyPageTab: ScreenRouteDef

    @Serializable
    data object StatisticsTab: ScreenRouteDef

    @Serializable
    sealed interface MemoManagementTab : ScreenRouteDef{
        @Serializable
        data class CreateMemo(val address: String): MemoManagementTab

        @Serializable
        data class UpdateMemo(val memoId: Int) : MemoManagementTab
    }

}