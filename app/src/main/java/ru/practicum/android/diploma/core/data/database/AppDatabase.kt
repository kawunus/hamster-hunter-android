package ru.practicum.android.diploma.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.core.data.database.entity.PlaceholderEntity

@Database(
    version = 1,
    entities = [PlaceholderEntity::class]
)
abstract class AppDatabase : RoomDatabase()
