package com.roman.mydemoapp.playlist

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.createSavedStateHandle
import com.roman.mydemoapp.R
import com.roman.mydemoapp.mainscreen.OptionCard
import com.roman.mydemoapp.roast.data.PlaylistRepositoryImpl
import com.roman.mydemoapp.roast.data.RoastRepositoryImpl
import com.roman.mydemoapp.roast.domain.model.RoastError
import com.roman.mydemoapp.roast.domain.model.RoastedPlaylist
import com.roman.mydemoapp.roast.domain.usecase.RoastPlaylistUseCase
import com.roman.mydemoapp.roast.domain.usecase.RoastScreenshotUseCase
import com.roman.mydemoapp.settings.data.SettingsDataSource
import com.roman.mydemoapp.settings.data.SettingsRepositoryImpl
import com.roman.mydemoapp.settings.domain.model.RoastLevel
import com.roman.mydemoapp.settings.domain.usecase.GetRoastLevelUseCase
import com.roman.mydemoapp.settings.presentation.title
import com.roman.mydemoapp.ui.theme.AppTheme

private val PlaylistCardColor = Color(0xFF1C1612)
private val CoverColor = Color(0xFF7B3A1C)
private val CoverIconColor = Color(0xFFF7C4A8)

@Composable
fun PlaylistScreen(
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit = {},
    onAnotherPlaylistClick: () -> Unit = onBackClick
) {
    val context = LocalContext.current
    val viewModel = viewModel {
        val getRoastLevel = GetRoastLevelUseCase(SettingsRepositoryImpl(SettingsDataSource(context)))
        val roastRepository = RoastRepositoryImpl(context)
        PlaylistViewModel(
            source = PlaylistSource.from(createSavedStateHandle()),
            roastPlaylist = RoastPlaylistUseCase(PlaylistRepositoryImpl(), roastRepository, getRoastLevel),
            roastScreenshot = RoastScreenshotUseCase(roastRepository, getRoastLevel)
        )
    }
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is PlaylistEvent.Share -> {
                    val send = Intent(Intent.ACTION_SEND)
                        .setType("text/plain")
                        .putExtra(Intent.EXTRA_TEXT, event.text)
                    context.startActivity(Intent.createChooser(send, null))
                }
            }
        }
    }
    PlaylistContent(
        state = state,
        onBackClick = onBackClick,
        onSettingsClick = onSettingsClick,
        onShareClick = viewModel::onShareClick,
        onRoastAgainClick = viewModel::onRoastAgainClick,
        onAnotherPlaylistClick = onAnotherPlaylistClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PlaylistContent(
    state: PlaylistUiState,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onRoastAgainClick: () -> Unit = {},
    onAnotherPlaylistClick: () -> Unit = onBackClick
) {
    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.playlist_title),
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.settings_back_description)
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = onSettingsClick) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = stringResource(R.string.main_settings_description)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            }
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                SourceChip(fromScreenshot = state.fromScreenshot)
                Spacer(Modifier.height(16.dp))
                val roasted = state.roasted
                when {
                    state.isLoading -> LoadingCard()
                    state.error != null -> ErrorCard(state.error, onRetryClick = onRoastAgainClick)
                    roasted != null -> {
                        PlaylistInfoCard(roasted)
                        Spacer(Modifier.height(16.dp))
                        VerdictCard(roasted, onShareClick = onShareClick, onRoastAgainClick = onRoastAgainClick)
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onAnotherPlaylistClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(20.dp))
                Text(
                    text = stringResource(R.string.playlist_another_button),
                    modifier = Modifier.padding(start = 12.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun SourceChip(fromScreenshot: Boolean) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
            Text(
                text = stringResource(
                    if (fromScreenshot) R.string.playlist_source_screenshot else R.string.playlist_source_share
                ),
                modifier = Modifier.padding(start = 8.dp),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun PlaylistInfoCard(roasted: RoastedPlaylist) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = PlaylistCardColor
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(88.dp),
                shape = RoundedCornerShape(16.dp),
                color = CoverColor
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(R.drawable.ic_music_note),
                        contentDescription = null,
                        tint = CoverIconColor,
                        modifier = Modifier.size(44.dp)
                    )
                }
            }
            Column(
                modifier = Modifier.padding(start = 16.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = stringResource(R.string.playlist_label),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f)
                )
                Text(
                    text = roasted.title ?: stringResource(R.string.playlist_unknown_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                roasted.trackCount?.let { count ->
                    Text(
                        text = LocalContext.current.resources.getQuantityString(R.plurals.playlist_tracks, count, count),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Composable
private fun VerdictCard(
    roasted: RoastedPlaylist,
    onShareClick: () -> Unit,
    onRoastAgainClick: () -> Unit
) {
    OptionCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(R.drawable.ic_flame),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Text(
                text = stringResource(R.string.playlist_verdict_title),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp),
                style = MaterialTheme.typography.titleLarge
            )
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(
                    text = stringResource(R.string.playlist_verdict_badge, stringResource(roasted.level.title), roasted.score),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = roasted.text,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            VerdictButton(R.string.playlist_share_button, Icons.Default.Share, onShareClick)
            VerdictButton(R.string.playlist_roast_again_button, Icons.Default.Refresh, onRoastAgainClick)
        }
    }
}

@Composable
private fun LoadingCard() {
    OptionCard {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator()
            Text(
                text = stringResource(R.string.playlist_loading),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun ErrorCard(error: RoastError, onRetryClick: () -> Unit) {
    OptionCard {
        Text(
            text = stringResource(error.message),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(16.dp))
        VerdictButton(R.string.playlist_retry_button, Icons.Default.Refresh, onRetryClick)
    }
}

@Composable
private fun VerdictButton(text: Int, icon: ImageVector, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = 14.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
        Text(
            text = stringResource(text),
            modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun PlaylistContentPreview() {
    AppTheme {
        PlaylistContent(
            state = PlaylistUiState(
                isLoading = false,
                roasted = RoastedPlaylist(
                    title = "Вечірній чіл",
                    trackCount = 42,
                    level = RoastLevel.Medium,
                    score = 3,
                    text = "Цей плейлист звучить як радіо в таксі, яке ніхто не наважується перемкнути."
                )
            ),
            onBackClick = {}
        )
    }
}
