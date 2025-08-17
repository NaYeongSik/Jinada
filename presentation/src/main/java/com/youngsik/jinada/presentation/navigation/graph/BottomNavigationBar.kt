package com.youngsik.jinada.presentation.navigation.graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun AppScaffold(
    navController: NavController,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        content(innerPadding)
    }
}


@Composable
fun BottomNavigationBar(navController: NavController){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        BottomNavigationItem().renderBottomNavigationItems()
            .forEachIndexed { _, item ->
                val color = MaterialTheme.colorScheme.primary
                NavigationBarItem(
                    selected = currentDestination?.route == item.route::class.qualifiedName,
                    label = {
                        Text(
                            text = item.tabName,
                            color = color
                        )
                    },
                    icon = {
                        Icon(
                            item.icon,
                            contentDescription = item.tabName,
                            tint = color
                        )
                    },
                    onClick = {
                        navController.navigate(item.route){
                            launchSingleTop = true
                        }
                    }
                )
            }
    }
}