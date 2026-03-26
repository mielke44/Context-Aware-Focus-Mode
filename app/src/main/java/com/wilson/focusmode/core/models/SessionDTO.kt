package com.wilson.focusmode.core.models

import com.google.gson.annotations.SerializedName

data class SessionDTO(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("start_timestamp")
    val startTimestamp: Long,
    @SerializedName("duration_seconds")
    val durationSeconds: Long,
    @SerializedName("sound_distractions")
    val soundDistractions: Int,
    @SerializedName("movement_distractions")
    val movementDistractions: Int
)