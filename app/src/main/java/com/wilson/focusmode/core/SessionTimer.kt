package com.wilson.focusmode.core

import android.os.SystemClock
import com.wilson.focusmode.core.interfaces.FocusTimer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion

class SessionTimer : FocusTimer {

    private var startTime: Long = 0L
    private var isRunning: Boolean = false

    override fun start(): Flow<Long> = flow {
        startTime = SystemClock.elapsedRealtime()
        isRunning = true

        while (isRunning) {
            val currentTime = SystemClock.elapsedRealtime()
            val elapsed = currentTime - startTime
            emit(elapsed)
            delay(1000)
        }
    }.onCompletion {
        isRunning = false
    }

    override fun stop() {
        isRunning = false
    }
}