package ru.practicum.android.diploma.vacancy.domain.model

data class VacancyDetails(
    val id: String,
    val name: String,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val currency: String,
    val employer: String,
    val area: String,
    val city: String,
    val street: String,
    val building: String,
    val experience: String,
    val employment: String,
    val workFormat: List<String>,
    val description: String,
    val keySkills: List<String>,
    val icon: String,
    val alternateUrl: String
)
