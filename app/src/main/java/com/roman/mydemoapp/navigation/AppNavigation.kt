package com.roman.mydemoapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.roman.mydemoapp.mainscreen.MainScreen
import com.roman.mydemoapp.settings.SettingsScreen

object Routes {
    const val MAIN = "main"
    const val SETTINGS = "settings"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.MAIN) {
        composable(Routes.MAIN) {
            MainScreen(onSettingsClick = { navController.navigate(Routes.SETTINGS) })
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(onBackClick = { navController.popBackStack() })
        }
    }
}
