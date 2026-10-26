package com.roman.mydemoapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.roman.mydemoapp.navigation.AppNavigation
import com.roman.mydemoapp.navigation.LinkViewModel
import com.roman.mydemoapp.navigation.extractPlaylistLink
import com.roman.mydemoapp.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    private val linkViewModel: LinkViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (savedInstanceState == null) linkViewModel.onNewLink(intent.extractPlaylistLink())
        setContent {
            AppTheme {
                AppNavigation(linkViewModel)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        linkViewModel.onNewLink(intent.extractPlaylistLink())
    }
}
