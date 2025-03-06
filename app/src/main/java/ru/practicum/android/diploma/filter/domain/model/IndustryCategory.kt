package ru.practicum.android.diploma.filter.domain.model

data class IndustryCategory(
    val id: Int,
    val name: String,
    val industries: List<Industry>
)
