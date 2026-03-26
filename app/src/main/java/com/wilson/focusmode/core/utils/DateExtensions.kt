package com.wilson.focusmode.core.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toReadableDate(): String {
    val date = Date(this)
    val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return formatter.format(date)
}