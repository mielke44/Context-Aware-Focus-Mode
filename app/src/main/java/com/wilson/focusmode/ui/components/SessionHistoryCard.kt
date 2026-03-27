package com.wilson.focusmode.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wilson.focusmode.R
import com.wilson.focusmode.core.models.FocusSessionEntity
import com.wilson.focusmode.core.models.formatTime
import com.wilson.focusmode.core.utils.toReadableDate

@Composable
fun SessionHistoryCard(
    session: FocusSessionEntity,
    accentColor: Color
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        ),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = session.startTimestamp.toReadableDate(),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )
                Text(
                    text = session.durationMillis.formatTime(),
                    style = MaterialTheme.typography.titleMedium,
                    color = accentColor,
                    fontWeight = FontWeight.Bold
                )
            }

            HorizontalDivider(color = Color.DarkGray, thickness = 0.5.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                DistractionStat(
                    label = stringResource(R.string.sound_label),
                    value = session.soundDistractions,
                    icon = Icons.Default.Mic,
                    accentColor = accentColor
                )
                DistractionStat(
                    label = stringResource(R.string.movement_label),
                    value = session.movementDistractions,
                    icon = Icons.Default.ScreenRotation,
                    accentColor = accentColor
                )
            }
        }
    }
}