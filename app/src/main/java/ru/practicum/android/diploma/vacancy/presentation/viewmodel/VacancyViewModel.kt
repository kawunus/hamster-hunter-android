package ru.practicum.android.diploma.vacancy.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.ui.BaseViewModel
import ru.practicum.android.diploma.favorites.domain.usecase.FavoriteVacancyInteractor
import ru.practicum.android.diploma.util.toVacancy
import ru.practicum.android.diploma.vacancy.domain.model.ErrorType
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetails
import ru.practicum.android.diploma.vacancy.domain.usecase.VacancyDetailsInteractor

class VacancyViewModel(
    private val favoriteVacancyInteractor: FavoriteVacancyInteractor,
    private val vacancyDetailsInteractor: VacancyDetailsInteractor,
    private val vacancyId: String
) : BaseViewModel() {
    private val vacancyDetailsLiveData = MutableLiveData<VacancyDetailsState>(VacancyDetailsState.Loading)
    private val vacancyIsLikedLiveData = MutableLiveData<Boolean?>(null)
    fun observeIsLikedLiveData(): LiveData<Boolean?> = vacancyIsLikedLiveData
    fun observeVacancyDetailsState(): LiveData<VacancyDetailsState> = vacancyDetailsLiveData

    init {
        vacancyDetailsLiveData.postValue(VacancyDetailsState.Loading)
        viewModelScope.launch {
            vacancyDetailsInteractor.findVacancy(vacancyId).collect { pair ->
                processResult(pair.first, pair.second)
            }
        }
    }

    private suspend fun processResult(vacancyDetails: VacancyDetails?, errorMessage: ErrorType?) {
        if (vacancyDetails != null && errorMessage != ErrorType.NOT_FOUND) {
            initIsVacancyInFavorite(vacancyDetails)
        } else if (vacancyDetails != null) {
            favoriteVacancyInteractor.deleteVacancyFromFavorites(vacancyDetails.id)
            vacancyDetailsLiveData.postValue(VacancyDetailsState.NotFoundError)
        } else {
            vacancyDetailsLiveData.postValue(VacancyDetailsState.ServerError)
        }
    }

    private suspend fun initIsVacancyInFavorite(vacancyDetails: VacancyDetails) {
        val isLiked = favoriteVacancyInteractor.isVacancyInFavorites(vacancyId)
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
            if (vacancyDetailsLiveData.value is VacancyDetailsState.VacancyInfo && vacancyId.isNotEmpty()) {
                favoriteVacancyInteractor.addVacancyToFavorites(vacancy.toVacancy())
                val newLikeStatus = favoriteVacancyInteractor.isVacancyInFavorites(vacancyId)
                vacancyIsLikedLiveData.postValue(newLikeStatus)
            }
        }
    }

    private fun deleteVacancyFromFavorites() {
        viewModelScope.launch {
            if (vacancyDetailsLiveData.value is VacancyDetailsState.VacancyInfo && vacancyId.isNotEmpty()) {
                favoriteVacancyInteractor.deleteVacancyFromFavorites(vacancyId)
                val newLikeStatus = favoriteVacancyInteractor.isVacancyInFavorites(vacancyId)
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
