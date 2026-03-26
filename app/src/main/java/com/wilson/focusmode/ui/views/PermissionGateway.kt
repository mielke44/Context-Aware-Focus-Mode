package com.wilson.focusmode.ui.views

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun FocusPermissionGateway(
    onPermissionsGranted: () -> Unit
) {

    val permissions = mutableListOf(Manifest.permission.RECORD_AUDIO)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        permissions.add(Manifest.permission.POST_NOTIFICATIONS)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { resultMap ->
        val allGranted = resultMap.values.all { it }
        if (allGranted) {
            onPermissionsGranted()
        } else {
            TODO("UX show necessity")
        }
    }

    LaunchedEffect(Unit) {
        launcher.launch(permissions.toTypedArray())
    }
}