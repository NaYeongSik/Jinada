package com.youngsik.jinada.nav_graph

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Description
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val tabName : String = "",
    val icon : ImageVector = Icons.Filled.ErrorOutline,
    val route : ScreenRouteDef = ScreenRouteDef.BottomNavigation.MainTab
) {
    fun renderBottomNavigationItems() : List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                tabName = "메인화면",
                icon = Icons.Filled.Home,
                route = ScreenRouteDef.BottomNavigation.MainTab
            ),
            BottomNavigationItem(
                tabName = "내 메모",
                icon = Icons.Outlined.Description,
                route = ScreenRouteDef.BottomNavigation.MyMemoTab
            ),
            BottomNavigationItem(
                tabName = "마이페이지",
                icon = Icons.Filled.Person,
                route = ScreenRouteDef.BottomNavigation.MyPageTab
            ),
            BottomNavigationItem(
                tabName = "통계",
                icon = Icons.Outlined.BarChart,
                route = ScreenRouteDef.BottomNavigation.StatisticsTab
            )
        )
    }
}