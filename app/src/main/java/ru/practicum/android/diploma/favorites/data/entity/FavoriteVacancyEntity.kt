package ru.practicum.android.diploma.favorites.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_vacancies")
data class FavoriteVacancyEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "salary_from") val salaryFrom: Int?,
    @ColumnInfo(name = "salary_to") val salaryTo: Int?,
    @ColumnInfo(name = "added_at") val addedAt: Long,
    val name: String,
    val currency: String,
    val employer: String,
    val area: String,
    val city: String,
    val street: String,
    val building: String,
    val experience: String,
    val employment: String,
    val workFormat: String,
    val description: String,
    val keySkills: String,
    val icon: String,
    val alternateUrl: String
)
