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
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetailsLikeState
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetailsState

class VacancyViewModel(
    private val favoriteVacancyInteractor: FavoriteVacancyInteractor,
    private val vacancyDetailsInteractor: VacancyDetailsInteractor,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    private val vacancyId = savedStateHandle.get<String>(KEY_ID)?.toIntOrNull() ?: 0
    private val vacancyDetailsLiveData = MutableLiveData<VacancyDetailsState>(VacancyDetailsState.Loading)
    private val vacancyDetailsLikeLiveData =
        MutableLiveData<VacancyDetailsLikeState>(VacancyDetailsLikeState.Uninitialized)

    fun observeVacancyDetailsState(): LiveData<VacancyDetailsState> = vacancyDetailsLiveData
    fun observeVacancyDetailsLikeState(): LiveData<VacancyDetailsLikeState> = vacancyDetailsLikeLiveData

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
        vacancyDetailsLikeLiveData.postValue(
            if (isLiked)
                VacancyDetailsLikeState.Liked
            else VacancyDetailsLikeState.NotLiked
        )
        vacancyDetailsLiveData.postValue(VacancyDetailsState.VacancyLiked(vacancyDetails))
    }

    fun likeControl() {
        val previousState = vacancyDetailsLiveData.value
        if (previousState is VacancyDetailsState.VacancyLiked) {
            when (vacancyDetailsLikeLiveData.value) {
                is VacancyDetailsLikeState.Liked -> deleteVacancyFromFavorites()
                is VacancyDetailsLikeState.NotLiked -> addVacancyToFavorites(previousState.details)
                else -> {}
            }
        }
    }

    private fun addVacancyToFavorites(vacancy: VacancyDetails) {
        viewModelScope.launch {
            if (vacancyDetailsLiveData.value is VacancyDetailsState.VacancyLiked && vacancyId != 0) {
                favoriteVacancyInteractor.addVacancyToFavorites(vacancy.toFavoriteVacancy())
                val newLikeStatus = favoriteVacancyInteractor.isVacancyInFavorites(vacancyId.toString())
                vacancyDetailsLikeLiveData.postValue(
                    if (newLikeStatus)
                        VacancyDetailsLikeState.Liked
                    else VacancyDetailsLikeState.NotLiked
                )
            }
        }
    }

    private fun deleteVacancyFromFavorites() {
        viewModelScope.launch {
            if (vacancyDetailsLiveData.value is VacancyDetailsState.VacancyLiked && vacancyId != 0) {
                favoriteVacancyInteractor.deleteVacancyFromFavorites(vacancyId.toString())
                val newLikeStatus = favoriteVacancyInteractor.isVacancyInFavorites(vacancyId.toString())
                vacancyDetailsLikeLiveData.postValue(
                    if (newLikeStatus) VacancyDetailsLikeState.Liked else VacancyDetailsLikeState.NotLiked
                )
            }
        }
    }

    fun shareControll() {
        val prevState = vacancyDetailsLiveData.value
        if (prevState is VacancyDetailsState.VacancyLiked) {
            vacancyDetailsInteractor.openVacancyShare(
                prevState.details.alternateUrl ?: SHAREPREFIX + vacancyId.toString()
            )
        }
    }

    companion object {
        const val KEY_ID = "vacancyId"
        const val SHAREPREFIX = "https://hh.ru/vacancy/"
    }

}
