package com.roman.mydemoapp.mainscreen

import android.text.format.DateUtils
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.roman.mydemoapp.R
import com.roman.mydemoapp.history.domain.model.SavedRoast
import com.roman.mydemoapp.settings.presentation.title

@Composable
internal fun RecentSection(
    items: List<SavedRoast>,
    onItemClick: (Long) -> Unit,
    onDeleteClick: (Long) -> Unit
) {
    Column(
        modifier = Modifier.padding(top = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.main_recent_title),
            style = MaterialTheme.typography.titleLarge
        )
        items.forEach { item ->
            RecentItem(item, onClick = { onItemClick(item.id) }, onDeleteClick = { onDeleteClick(item.id) })
        }
    }
}

@Composable
private fun RecentItem(item: SavedRoast, onClick: () -> Unit, onDeleteClick: () -> Unit) {
    val roasted = item.roasted
    val badge = stringResource(R.string.playlist_verdict_badge, stringResource(roasted.level.title), roasted.score)
    val age = DateUtils.getRelativeTimeSpanString(
        item.createdAt,
        System.currentTimeMillis(),
        DateUtils.MINUTE_IN_MILLIS
    )
    OptionCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = roasted.title ?: stringResource(R.string.playlist_unknown_title),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "$badge · $age",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.main_recent_delete_description)
                )
            }
        }
    }
}
