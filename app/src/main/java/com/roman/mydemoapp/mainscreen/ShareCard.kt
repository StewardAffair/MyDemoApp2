package com.roman.mydemoapp.mainscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roman.mydemoapp.R

@Composable
fun ShareCard(onPasteLink: () -> Unit = {}) {
    OptionCard {
        OptionHeader(
            title = stringResource(R.string.share_card_title),
            description = stringResource(R.string.share_card_description),
            iconTileColor = MaterialTheme.colorScheme.secondaryContainer
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.size(26.dp)
            )
        }
        StepsPanel(
            steps = listOf(
                stringResource(R.string.share_card_step_1),
                stringResource(R.string.share_card_step_2),
                stringResource(R.string.share_card_step_3)
            )
        )
        Spacer(Modifier.height(16.dp))
        OutlinedButton(
            onClick = onPasteLink,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_link),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.size(8.dp))
            Text(
                text = stringResource(R.string.share_card_button),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun StepsPanel(steps: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        steps.forEachIndexed { index, text ->
            StepRow(number = index + 1, text = text)
        }
    }
}

@Composable
private fun StepRow(number: Int, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number.toString(),
                color = MaterialTheme.colorScheme.onSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = text,
            modifier = Modifier.padding(start = 12.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}
