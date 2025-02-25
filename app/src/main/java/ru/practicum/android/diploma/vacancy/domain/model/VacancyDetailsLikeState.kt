package ru.practicum.android.diploma.vacancy.domain.model

sealed class VacancyDetailsLikeState {
    object Uninitialized : VacancyDetailsLikeState()
    object Liked : VacancyDetailsLikeState()
    object NotLiked : VacancyDetailsLikeState()
}
