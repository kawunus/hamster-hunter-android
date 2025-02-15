package ru.practicum.android.diploma.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = []
)
abstract class AppDatabase : RoomDatabase()
