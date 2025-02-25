package ru.practicum.android.diploma.vacancy.presentation.viewmodel

import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetails

sealed class VacancyDetailsState {
    object Loading : VacancyDetailsState()
    object ServerError : VacancyDetailsState()
    object NotFoundError : VacancyDetailsState()
    data class VacancyInfo(val details: VacancyDetails) : VacancyDetailsState()
}
