package com.wilson.focusmode.core.models

import java.util.Locale

data class FocusUiState(
    val isActive: Boolean = false,
    val durationMillis: Long = 0L,
    val soundDistractions: Int = 0,
    val movementDistractions: Int = 0
)

fun Long.formatTime(): String {
    val seconds = (this / 1000) % 60
    val minutes = (this / (1000 * 60)) % 60
    val hours = (this / (1000 * 60 * 60))
    return String.format(locale = Locale.US,"%02d:%02d:%02d", hours, minutes, seconds)
}