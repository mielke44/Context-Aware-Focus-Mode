package com.wilson.focusmode.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.wilson.focusmode.ui.theme.FocusModeTheme
import com.wilson.focusmode.ui.views.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FocusModeTheme {
                AppNavigation()
            }
        }
    }
}