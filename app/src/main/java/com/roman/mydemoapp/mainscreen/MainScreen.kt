package com.roman.mydemoapp.mainscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Просмажка") },
                navigationIcon = {
                    IconButton(onClick = { /* Дія назад */ }) {
                        Icon(Icons.Default.Build, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Дія пошуку */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Пошук")
                    }
                }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier.padding(
                top = contentPadding.calculateTopPadding(),
                start = 16.dp,
                end = 16.dp
            )
        ) {
            Text(
                "Дай нам свій плейліст. Ми його просмажимо",
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(Modifier.height(height = 12.dp))
            Text(
                "AI честно скаже, що думає про твій музичний смак. Обери як додати плейліст:",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(height = 12.dp))
            ScreenshotCard()
        }
    }

}