package com.wilson.focusmode.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wilson.focusmode.R
import com.wilson.focusmode.core.FocusRepository
import com.wilson.focusmode.core.interfaces.DistractionSensor
import com.wilson.focusmode.core.interfaces.FocusServiceController
import com.wilson.focusmode.core.interfaces.FocusTimer
import com.wilson.focusmode.core.models.DistractionType
import com.wilson.focusmode.core.models.FocusSessionEntity
import com.wilson.focusmode.core.models.FocusUiState
import com.wilson.focusmode.core.utils.NotificationUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BaseViewModel(
    private val serviceController: FocusServiceController,
    private val repository: FocusRepository
): ViewModel() {
    val history: StateFlow<List<FocusSessionEntity>> = repository.getSessionHistory()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val uiState: StateFlow<FocusUiState> = repository.activeSessionState
        .map { data ->
            FocusUiState(
                durationMillis = data.durationMillis,
                soundDistractions = data.soundDistractions,
                movementDistractions = data.movementDistractions,
                isActive = data.durationMillis > 0
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FocusUiState()
        )

    fun onStartClicked() = serviceController.startService()

    fun onStopClicked() {
        serviceController.stopService()
        repository.resetActiveSession()
    }
}