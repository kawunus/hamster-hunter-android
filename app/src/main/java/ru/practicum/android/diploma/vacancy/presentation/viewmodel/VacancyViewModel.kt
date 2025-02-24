package ru.practicum.android.diploma.vacancy.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.ui.BaseViewModel
import ru.practicum.android.diploma.favorites.domain.api.FavoriteVacancyInteractor
import ru.practicum.android.diploma.util.toFavoriteVacancy
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetails
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetailsState

class VacancyViewModel(
    private val favoriteVacancyInteractor: FavoriteVacancyInteractor,
    private val vacancyDetailsInteractor: VacancyDetailsInteractor,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    private val vacancyId = savedStateHandle.get<String>(KEY_ID)?.toIntOrNull() ?: 0
    private val vacancyDetailsLiveData = MutableLiveData<VacancyDetailsState>(VacancyDetailsState.Loading)
    fun observeVacancyDetailsState(): LiveData<VacancyDetailsState> = vacancyDetailsLiveData

    init {
        if (vacancyId != 0) { // такой вакансии нет
            vacancyDetailsLiveData.postValue(VacancyDetailsState.Loading)
            viewModelScope.launch {
                vacancyDetailsInteractor.findVacancy(vacancyId).collect { foundData ->
                    if (foundData != null) { //есть данные
                        initIsVacancyInFavorite(foundData)
                    } else vacancyDetailsLiveData.postValue( //обобщенный сигнал ошибки
                        VacancyDetailsState.ServerError
                    )
                    //ДОБАВИТЬ ПОСЛЕ 53 таски
                    /*
                    ошибка сервера -> {
                        vacancyDetailsLiveData.postValue(VacancyDetailsState.ServerError)
                    }
                    Нет записи в базах -> {
                        favoriteVacancyInteractor.deleteVacancyFromFavorites(vacancyId) // удаление из локальной базы, если его убрали в НН
                        vacancyDetailsLiveData.postValue(VacancyDetailsState.NotFoundError)
                    }
                }*/
                }

            }
        }
    }

    private suspend fun initIsVacancyInFavorite(vacancyDetails: VacancyDetails) {
        val isLiked = favoriteVacancyInteractor.isVacancyInFavorites(vacancyId.toString())
        vacancyDetailsLiveData.postValue(VacancyDetailsState.VacancyLiked(vacancyDetails, isLiked))
    }

    fun likeButtonControl() {
        val previousState = vacancyDetailsLiveData.value
        if (previousState is VacancyDetailsState.VacancyLiked) {
            val vacancy = previousState.details
            if (previousState.isLiked == true) {
                deleteVacancyFromFavorites()
            } else {
                addVacancyToFavorites(vacancy)
            }
        }
    }

    private fun addVacancyToFavorites(vacancy: VacancyDetails) {
        viewModelScope.launch {
            val previousState = vacancyDetailsLiveData.value
            if (previousState is VacancyDetailsState.VacancyLiked && vacancyId != 0) {
                favoriteVacancyInteractor.addVacancyToFavorites(vacancy.toFavoriteVacancy())
                val newLikeStatus = favoriteVacancyInteractor.isVacancyInFavorites(vacancyId.toString())
                vacancyDetailsLiveData.postValue(VacancyDetailsState.VacancyLiked(previousState.details, newLikeStatus))
            }
        }
    }

    private fun deleteVacancyFromFavorites() {
        viewModelScope.launch {
            val previousState = vacancyDetailsLiveData.value
            if (previousState is VacancyDetailsState.VacancyLiked && vacancyId != 0) {
                favoriteVacancyInteractor.deleteVacancyFromFavorites(vacancyId.toString())
                val newLikeStatus = favoriteVacancyInteractor.isVacancyInFavorites(vacancyId.toString())
                vacancyDetailsLiveData.postValue(VacancyDetailsState.VacancyLiked(previousState.details, newLikeStatus))
            }
        }
    }

    fun shareButtonControll() {
        vacancyDetailsInteractor.openVacancyShare(vacancyId.toString())
    }

    companion object {
        const val KEY_ID = "vacancyId"
    }

}
