package com.roman.mydemoapp.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
fun AppNavigation(linkViewModel: LinkViewModel) {
    val navController = rememberNavController()
    val incomingLink by linkViewModel.incomingLink.collectAsStateWithLifecycle()
    LaunchedEffect(incomingLink) {
        incomingLink?.let { link ->
            navController.navigate(Routes.playlist(link))
            linkViewModel.onLinkHandled()
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
        ) {
            PlaylistScreen(
                onBackClick = { navController.popBackStack() },
                onSettingsClick = { navController.navigate(Routes.SETTINGS) }
            )
        }
    }
}
