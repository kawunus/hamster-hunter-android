package ru.practicum.android.diploma.vacancy.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.ui.BaseViewModel
import ru.practicum.android.diploma.favorites.domain.api.FavoriteVacancyInteractor
import ru.practicum.android.diploma.util.toVacancy
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetails

class VacancyViewModel(
    private val favoriteVacancyInteractor: FavoriteVacancyInteractor,
    private val vacancyDetailsInteractor: VacancyDetailsInteractor,
    private val vacancyId: Int
) : BaseViewModel() {
    private val vacancyDetailsLiveData = MutableLiveData<VacancyDetailsState>(VacancyDetailsState.Loading)
    private val vacancyIsLikedLiveData = MutableLiveData<Boolean?>(null)
    fun observeIsLikedLiveData(): LiveData<Boolean?> = vacancyIsLikedLiveData
    fun observeVacancyDetailsState(): LiveData<VacancyDetailsState> = vacancyDetailsLiveData

    init {
        if (vacancyId != 0) { // такой вакансии нет
            vacancyDetailsLiveData.postValue(VacancyDetailsState.Loading)
            viewModelScope.launch {
                vacancyDetailsInteractor.findVacancy(vacancyId).collect { foundData ->
                    if (foundData != null) { // есть данные
                        initIsVacancyInFavorite(foundData)
                    } else {
                        vacancyDetailsLiveData.postValue( // обобщенный сигнал ошибки
                            VacancyDetailsState.ServerError
                        )
                    }
                    // ДОБАВИТЬ ПОСЛЕ 53 таски
                    /*
                    ошибка сервера -> {
                        vacancyDetailsLiveData.postValue(VacancyDetailsState.ServerError)
                    }
                    Нет записи в базах -> { // удаление из локальной базы, если его убрали в НН
                        favoriteVacancyInteractor.deleteVacancyFromFavorites(vacancyId)
                        vacancyDetailsLiveData.postValue(VacancyDetailsState.NotFoundError)
                    }
                } */
                }
            }
        } else {
            // инет пропал
        }
    }

    private suspend fun initIsVacancyInFavorite(vacancyDetails: VacancyDetails) {
        val isLiked = favoriteVacancyInteractor.isVacancyInFavorites(vacancyId.toString())
        vacancyIsLikedLiveData.postValue(isLiked)
        vacancyDetailsLiveData.postValue(VacancyDetailsState.VacancyInfo(vacancyDetails))
    }

    fun likeControl() {
        val previousState = vacancyDetailsLiveData.value
        if (previousState is VacancyDetailsState.VacancyInfo) {
            when (vacancyIsLikedLiveData.value) {
                true -> deleteVacancyFromFavorites()
                false -> addVacancyToFavorites(previousState.details)
                else -> {}
            }
        }
    }

    private fun addVacancyToFavorites(vacancy: VacancyDetails) {
        viewModelScope.launch {
            if (vacancyDetailsLiveData.value is VacancyDetailsState.VacancyInfo && vacancyId != 0) {
                favoriteVacancyInteractor.addVacancyToFavorites(vacancy.toVacancy())
                val newLikeStatus = favoriteVacancyInteractor.isVacancyInFavorites(vacancyId.toString())
                vacancyIsLikedLiveData.postValue(newLikeStatus)
            }
        }
    }

    private fun deleteVacancyFromFavorites() {
        viewModelScope.launch {
            if (vacancyDetailsLiveData.value is VacancyDetailsState.VacancyInfo && vacancyId != 0) {
                favoriteVacancyInteractor.deleteVacancyFromFavorites(vacancyId.toString())
                val newLikeStatus = favoriteVacancyInteractor.isVacancyInFavorites(vacancyId.toString())
                vacancyIsLikedLiveData.postValue(newLikeStatus)
            }
        }
    }

    fun shareVacancyUrl() {
        val prevState = vacancyDetailsLiveData.value
        if (prevState is VacancyDetailsState.VacancyInfo) {
            vacancyDetailsInteractor.openVacancyShare(prevState.details.alternateUrl)
        }
    }

}
