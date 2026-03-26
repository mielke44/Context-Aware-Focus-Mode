package com.wilson.focusmode.core.models

sealed class DistractionType {
    object Sound : DistractionType()
    object Movement : DistractionType()
}