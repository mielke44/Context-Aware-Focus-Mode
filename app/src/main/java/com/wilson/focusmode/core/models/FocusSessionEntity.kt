package com.wilson.focusmode.core.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "focus_sessions")
data class FocusSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "start_timestamp") val startTimestamp: Long,
    @ColumnInfo(name = "duration_millis") val durationMillis: Long,
    @ColumnInfo(name = "sound_distractions") val soundDistractions: Int,
    @ColumnInfo(name = "movement_distractions") val movementDistractions: Int
)

fun FocusSessionEntity.toDto(): SessionDTO {
    return SessionDTO(
        startTimestamp = this.startTimestamp,
        durationSeconds = this.durationMillis / 1000,
        soundDistractions = this.soundDistractions,
        movementDistractions = this.movementDistractions
    )
}
