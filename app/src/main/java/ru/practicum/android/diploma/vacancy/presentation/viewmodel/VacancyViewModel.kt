package ru.practicum.android.diploma.vacancy.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.ui.BaseViewModel
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetails
import ru.practicum.android.diploma.vacancy.domain.usecase.GetVacancyUseCase
import ru.practicum.android.diploma.favorites.domain.api.FavoriteVacancyInteractor
import ru.practicum.android.diploma.search.domain.model.Vacancy

class VacancyViewModel(
  private val favoriteVacancyInteractor: FavoriteVacancyInteractor,
  private val getVacancyUseCase: GetVacancyUseCase
) : BaseViewModel() {
 
    private val vacancyLiveData = MutableLiveData<VacancyDetails>()
    val observeVacancy: LiveData<VacancyDetails> = vacancyLiveData
    private val isFavoriteLiveData = MutableLiveData<Boolean>()
    fun observeIsFavoriteState(): LiveData<Boolean> = isFavoriteLiveData

    fun getVacancy(vacancyId: Int) {
        viewModelScope.launch {
            vacancyLiveData.value = getVacancyUseCase.execute(vacancyId)
        }
    }

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
