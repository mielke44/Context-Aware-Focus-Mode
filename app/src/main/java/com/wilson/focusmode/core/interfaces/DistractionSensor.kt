package com.wilson.focusmode.core.interfaces

import kotlinx.coroutines.flow.Flow

interface DistractionSensor<out T> {
    fun startListening(): Flow<T>
    fun stopListening()
}