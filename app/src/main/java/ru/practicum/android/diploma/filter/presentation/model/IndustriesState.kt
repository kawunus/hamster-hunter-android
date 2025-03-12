package ru.practicum.android.diploma.filter.presentation.model

import ru.practicum.android.diploma.filter.domain.model.Industry

sealed interface IndustriesState {
    data object Loading : IndustriesState
    data class Success(val industriesList: List<Industry>) : IndustriesState
    sealed class Error : IndustriesState {
        data object NetworkError : Error()
        data object ServerError : Error()
        data object NothingFound : Error()
    }
}
