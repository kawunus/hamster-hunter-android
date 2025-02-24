package ru.practicum.android.diploma.favorites.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_vacancies")
data class FavoriteVacancyEntity(
    @PrimaryKey val id: String,
    val name: String,
    val company: String,
    val currency: String,
    @ColumnInfo(name = "salary_from") val salaryFrom: Int?,
    @ColumnInfo(name = "salary_to") val salaryTo: Int?,
    val area: String,
    val icon: String,
    @ColumnInfo(name = "added_at") val addedAt: Long
)
