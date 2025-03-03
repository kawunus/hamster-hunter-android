package ru.practicum.android.diploma.filter.presentation.model

sealed interface RegionScreenState {
    data object Loading : RegionScreenState
    data object Content : RegionScreenState
    sealed class Error : RegionScreenState {
        data object ServerError : Error()
        data object NetworkError : Error()
    }
}
