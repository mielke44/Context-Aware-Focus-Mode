package com.wilson.focusmode.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wilson.focusmode.core.FocusRepository
import com.wilson.focusmode.core.interfaces.FocusServiceController
import com.wilson.focusmode.core.models.FocusSessionEntity
import com.wilson.focusmode.core.models.FocusUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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
    }

    fun onClearHistoryClicked(){
        CoroutineScope(Dispatchers.IO).launch{
            repository.flushDataBase()
        }
    }
}