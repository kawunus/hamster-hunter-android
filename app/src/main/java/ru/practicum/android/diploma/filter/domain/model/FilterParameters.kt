package ru.practicum.android.diploma.filter.domain.model

data class FilterParameters(
    val area: Area? = null,
    val industry: Industry? = null,
    val salary: Int? = null,
    val onlyWithSalary: Boolean? = null,
    val onlyInTitles: Boolean? = null
)
