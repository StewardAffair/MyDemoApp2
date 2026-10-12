package com.roman.mydemoapp.mainscreen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roman.mydemoapp.R

@Composable
fun ScreenshotCard(onPickFromGallery: () -> Unit = {}) {
    OptionCard {
        OptionHeader(
            title = stringResource(R.string.screenshot_card_title),
            description = stringResource(R.string.screenshot_card_description),
            iconTileColor = MaterialTheme.colorScheme.primaryContainer
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_ticket),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(26.dp)
            )
        }
        Button(
            onClick = onPickFromGallery,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_image),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.size(8.dp))
            Text(
                text = stringResource(R.string.screenshot_card_button),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
