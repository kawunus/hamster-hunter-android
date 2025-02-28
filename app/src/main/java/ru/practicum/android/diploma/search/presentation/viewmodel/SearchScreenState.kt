package ru.practicum.android.diploma.search.presentation.viewmodel

sealed interface SearchScreenState {
    data object Default : SearchScreenState
    data object Loading : SearchScreenState
    sealed class Error : SearchScreenState
    data object ServerError : Error()
    data object NothingFound : Error()
    data object NetworkError : Error()
    data object SearchResults : SearchScreenState
}
