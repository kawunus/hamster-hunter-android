package ru.practicum.android.diploma.filter.domain.model

data class FilterParameters(
    val area: String? = null,
    val professionalRole: String? = null,
    val onlyWithSalary: Boolean? = null,
    val onlyInTitles: Boolean? = null
)
