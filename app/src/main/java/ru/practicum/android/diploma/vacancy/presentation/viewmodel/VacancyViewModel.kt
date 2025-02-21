package ru.practicum.android.diploma.vacancy.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.ui.BaseViewModel
import ru.practicum.android.diploma.favorites.domain.api.FavoriteVacancyInteractor
import ru.practicum.android.diploma.search.domain.model.Vacancy

class VacancyViewModel(private val favoriteVacancyInteractor: FavoriteVacancyInteractor) : BaseViewModel() {

    private val isFavoriteLiveData = MutableLiveData<Boolean>()
    fun observeIsFavoriteState(): LiveData<Boolean> = isFavoriteLiveData

    fun likeButtonControl(vacancy: Vacancy) {
        if (isFavoriteLiveData.value == true) {
            deleteVacancyFromFavorites(vacancy)
        } else {
            addVacancyToFavorites(vacancy)
        }
    }

    private fun addVacancyToFavorites(vacancy: Vacancy) {
        viewModelScope.launch {
            favoriteVacancyInteractor.addVacancyToFavorites(vacancy)
            isFavoriteLiveData.value = favoriteVacancyInteractor.isVacancyInFavorites(vacancy.id)
        }
    }

    private fun deleteVacancyFromFavorites(vacancy: Vacancy) {
        viewModelScope.launch {
            favoriteVacancyInteractor.deleteVacancyFromFavorites(vacancy.id)
            isFavoriteLiveData.value = favoriteVacancyInteractor.isVacancyInFavorites(vacancy.id)
        }
    }

    fun initIsVacancyInFavorite(vacancyId: String) {
        viewModelScope.launch {
            isFavoriteLiveData.value = favoriteVacancyInteractor.isVacancyInFavorites(vacancyId)
        }
    }
}
