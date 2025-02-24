package ru.practicum.android.diploma.favorites.presentation.model

import ru.practicum.android.diploma.favorites.domain.model.FavoriteVacancy

sealed interface FavoritesState {

    data object Loading : FavoritesState

    data class Content(val vacanciesList: List<FavoriteVacancy>) : FavoritesState

    data object Empty : FavoritesState
}
