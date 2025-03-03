package ru.practicum.android.diploma.favorites.presentation.model

import ru.practicum.android.diploma.favorites.domain.model.FavoritesVacancy

sealed interface FavoritesState {

    data object Loading : FavoritesState

    data class Content(val vacanciesList: List<FavoritesVacancy>) : FavoritesState

    data object Empty : FavoritesState
}
