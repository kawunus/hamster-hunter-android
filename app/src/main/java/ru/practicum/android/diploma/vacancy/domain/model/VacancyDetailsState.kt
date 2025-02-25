package ru.practicum.android.diploma.vacancy.domain.model

sealed class VacancyDetailsState {
    object Loading : VacancyDetailsState()
    object ServerError : VacancyDetailsState()
    object NotFoundError : VacancyDetailsState()
    data class VacancyLiked(val details: VacancyDetails) : VacancyDetailsState()
}
