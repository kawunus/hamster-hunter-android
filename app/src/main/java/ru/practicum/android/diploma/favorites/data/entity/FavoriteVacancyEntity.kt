package ru.practicum.android.diploma.favorites.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.practicum.android.diploma.search.data.dto.Area
import ru.practicum.android.diploma.search.data.dto.Employer
import ru.practicum.android.diploma.search.data.dto.Salary

@Entity(tableName = "favorite_vacancies")
data class FavoriteVacancyEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String?,
    @ColumnInfo(name = "vacancy_name") val name: String?,
    @ColumnInfo(name = "salary") val salary: Salary?,
    @ColumnInfo(name = "employer") val employer: Employer?,
    @ColumnInfo(name = "area") val area: Area?,
    @ColumnInfo(name = "added_at") val addedAt: Long
)
