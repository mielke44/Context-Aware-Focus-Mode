package com.wilson.focusmode.core

import com.wilson.focusmode.core.database.FocusSessionDao
import com.wilson.focusmode.core.models.FocusSessionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FocusRepository(private val dao: FocusSessionDao) {
    private val _activeSessionState = MutableStateFlow(ActiveSessionData())
    val activeSessionState = _activeSessionState.asStateFlow()

    fun updateActiveSession(duration: Long, sounds: Int, movements: Int) {
        _activeSessionState.update {
            it.copy(durationMillis = duration, soundDistractions = sounds, movementDistractions = movements)
        }
    }

    fun resetActiveSession() {
        _activeSessionState.value = ActiveSessionData()
    }

    suspend fun saveSession(entity: FocusSessionEntity) = dao.insertSession(entity)

    fun getSessionHistory(): Flow<List<FocusSessionEntity>> = dao.getAllSessions()
}

data class ActiveSessionData(
    val durationMillis: Long = 0L,
    val soundDistractions: Int = 0,
    val movementDistractions: Int = 0
)