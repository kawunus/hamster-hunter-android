package ru.practicum.android.diploma.search.presentation.model

import androidx.paging.PagingData
import ru.practicum.android.diploma.search.domain.model.Vacancy

sealed interface SearchScreenState {
    data object Default : SearchScreenState
    data object Loading : SearchScreenState
    sealed class Error : SearchScreenState
    data object ServerError : Error()
    data object NothingFound : Error()
    data object NetworkError : Error()
    data class SearchResults(val data: PagingData<Vacancy>) : SearchScreenState
}
