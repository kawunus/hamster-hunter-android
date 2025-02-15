package ru.practicum.android.diploma.core.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Временный класс, для того, чтобы ksp не ругался
 */
@Entity
data class PlaceholderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
