package ru.practicum.android.diploma.favorites.presentation.model

import ru.practicum.android.diploma.search.domain.model.Vacancy

sealed interface FavoritesState {

    data object Loading : FavoritesState

    data class Content(val vacanciesList: List<Vacancy>) : FavoritesState

    data object Empty : FavoritesState
}
