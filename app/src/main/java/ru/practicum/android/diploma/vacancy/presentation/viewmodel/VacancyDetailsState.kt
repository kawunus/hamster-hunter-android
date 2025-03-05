package ru.practicum.android.diploma.vacancy.presentation.viewmodel

import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetails

sealed interface VacancyDetailsState {
    data object Loading : VacancyDetailsState
    data object ServerError : VacancyDetailsState
    data object NotFoundError : VacancyDetailsState
    data object NoInternet : VacancyDetailsState
    data class VacancyInfo(val details: VacancyDetails) : VacancyDetailsState
}
