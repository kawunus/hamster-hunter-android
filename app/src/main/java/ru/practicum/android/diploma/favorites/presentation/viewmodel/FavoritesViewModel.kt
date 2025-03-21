package ru.practicum.android.diploma.favorites.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.ui.BaseViewModel
import ru.practicum.android.diploma.favorites.domain.model.FavoritesVacancy
import ru.practicum.android.diploma.favorites.domain.usecase.FavoriteVacancyInteractor
import ru.practicum.android.diploma.favorites.presentation.model.FavoritesState

class FavoritesViewModel(private val favoriteVacancyInteractor: FavoriteVacancyInteractor) : BaseViewModel() {

    private val stateLiveData = MutableLiveData<FavoritesState>()
    fun observeState(): LiveData<FavoritesState> = stateLiveData

    fun getData() {
        renderState(FavoritesState.Loading)
        viewModelScope.launch {
            favoriteVacancyInteractor.getFavoriteVacancies().collect { vacanciesList ->
                processResult(vacanciesList)
            }
        }
    }

    private fun processResult(vacanciesList: List<FavoritesVacancy>) {
        if (vacanciesList.isEmpty()) {
            renderState(FavoritesState.Empty)
        } else {
            renderState(FavoritesState.Content(vacanciesList))
        }
    }

    private fun renderState(state: FavoritesState) {
        stateLiveData.value = state
    }
}
