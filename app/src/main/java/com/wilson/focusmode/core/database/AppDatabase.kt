package com.wilson.focusmode.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wilson.focusmode.core.models.FocusSessionEntity

@Database(entities = [FocusSessionEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun focusSessionDao(): FocusSessionDao
}