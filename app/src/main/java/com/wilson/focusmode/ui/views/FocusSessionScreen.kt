package com.wilson.focusmode.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wilson.focusmode.core.models.FocusUiState
import com.wilson.focusmode.core.models.formatTime
import com.wilson.focusmode.ui.components.ActionButton

@Composable
fun FocusSessionScreen(
    uiState: FocusUiState,
    onStartSession: () -> Unit,
    onStopSession: () -> Unit,
    onNavigateToStats: () -> Unit
) {

    val customColorScheme = darkColorScheme(
        background = Color(0xFF121212),
        surface = Color(0xFF1E1E1E),
        primary = Color(0xFF81C784),
        onPrimary = Color.Black,
        secondary = Color.Gray
    )

    MaterialTheme(colorScheme = customColorScheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Focus Session",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )

                    IconButton(
                        onClick = onNavigateToStats,
                        modifier = Modifier
                    ) {
                        Icon(
                            imageVector = Icons.Default.Insights,
                            contentDescription = "Ver histórico de sessões",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = uiState.durationMillis.formatTime(),
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontSize = 64.sp,
                            fontWeight = FontWeight.Light
                        ),
                        color = Color.White
                    )

                    Text(
                        text = "Decorrido",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                ActionButton(
                    isActive = uiState.isActive,
                    onStart = onStartSession,
                    onStop = onStopSession
                )
            }
        }
    }
}