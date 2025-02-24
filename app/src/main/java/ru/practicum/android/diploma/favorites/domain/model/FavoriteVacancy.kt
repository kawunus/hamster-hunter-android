package ru.practicum.android.diploma.favorites.domain.model

data class FavoriteVacancy(
    val id: String,
    val name: String,
    val company: String,
    val currency: String,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val area: String,
    val icon: String
)
