package com.wilson.focusmode.core.interfaces

import kotlinx.coroutines.flow.Flow

interface FocusTimer {
    fun start(): Flow<Long>
    fun stop()
}