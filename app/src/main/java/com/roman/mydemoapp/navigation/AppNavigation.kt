package com.roman.mydemoapp.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.roman.mydemoapp.mainscreen.MainScreen
import com.roman.mydemoapp.playlist.PlaylistScreen
import com.roman.mydemoapp.settings.SettingsScreen

object Routes {
    const val MAIN = "main"
    const val SETTINGS = "settings"
    const val PLAYLIST_ARG = "url"
    const val PLAYLIST = "playlist/{$PLAYLIST_ARG}"

    fun playlist(url: String) = "playlist/${Uri.encode(url)}"
}

@Composable
fun AppNavigation(incomingLink: String?, onIncomingLinkHandled: () -> Unit) {
    val navController = rememberNavController()
    LaunchedEffect(incomingLink) {
        if (incomingLink != null) {
            navController.navigate(Routes.playlist(incomingLink))
            onIncomingLinkHandled()
        }
    }
    NavHost(navController = navController, startDestination = Routes.MAIN) {
        composable(Routes.MAIN) {
            MainScreen(onSettingsClick = { navController.navigate(Routes.SETTINGS) })
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(onBackClick = { navController.popBackStack() })
        }
        composable(
            route = Routes.PLAYLIST,
            arguments = listOf(navArgument(Routes.PLAYLIST_ARG) { type = NavType.StringType })
        ) { entry ->
            PlaylistScreen(
                url = entry.arguments?.getString(Routes.PLAYLIST_ARG).orEmpty(),
                onBackClick = { navController.popBackStack() },
                onSettingsClick = { navController.navigate(Routes.SETTINGS) }
            )
        }
    }
}
