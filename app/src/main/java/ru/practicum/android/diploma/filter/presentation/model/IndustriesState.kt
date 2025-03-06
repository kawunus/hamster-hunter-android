package ru.practicum.android.diploma.filter.presentation.model

import ru.practicum.android.diploma.filter.domain.model.Industry

sealed interface IndustriesState {
    data object Loading : IndustriesState
    data class Success(val industriesList: List<Industry>) : IndustriesState
    data object NetworkError : IndustriesState
    data object ServerError : IndustriesState
    data object NothingFound : IndustriesState
}
