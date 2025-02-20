package ru.practicum.android.diploma.search.domain.model

data class Vacancy(
    val id: String,
    val name: String,
    val company: String,
    val currency: String,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val area: String,
    val icon: String
)
