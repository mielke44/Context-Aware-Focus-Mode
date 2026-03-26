package com.wilson.focusmode.ui.views

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wilson.focusmode.core.utils.NotificationUtil
import com.wilson.focusmode.ui.viewmodel.BaseViewModel
import org.koin.androidx.compose.get
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()){

    val baseViewModel: BaseViewModel = koinViewModel()
    val notificationUtil: NotificationUtil = get()

    NavHost(navController = navController, startDestination = Screen.Permissions) {

        composable(Screen.Permissions) {
            FocusPermissionGateway(
                onPermissionsGranted = {
                    notificationUtil.createFocusChannel()
                    navController.navigate(Screen.FocusSession) {
                        popUpTo(Screen.Permissions) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.FocusSession) {
            val uiState = baseViewModel.uiState.collectAsStateWithLifecycle().value

            FocusSessionScreen(
                uiState = uiState,
                onStartSession = { baseViewModel.onStartClicked() },
                onStopSession = { baseViewModel.onStopClicked() },
                onNavigateToStats = { navController.navigate(Screen.Stats) }
            )
        }

        composable(Screen.Stats) {
            SessionStatsScreen(
                onBack = { navController.popBackStack() },
                viewModel = baseViewModel,
            )
        }
    }
}

object Screen {
    const val Permissions = "permissions_check"
    const val FocusSession = "focus_session"
    const val Stats = "session_stats"
}