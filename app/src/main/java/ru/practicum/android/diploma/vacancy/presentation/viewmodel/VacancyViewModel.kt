package ru.practicum.android.diploma.vacancy.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.ui.BaseViewModel
import ru.practicum.android.diploma.favorites.domain.api.FavoriteVacancyInteractor
import ru.practicum.android.diploma.search.domain.model.Vacancy
import ru.practicum.android.diploma.sharing.domain.api.SharingInteractor
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetails
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetailsState

class VacancyViewModel(
    private val favoriteVacancyInteractor: FavoriteVacancyInteractor,
    private val sharingInteractor: SharingInteractor,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val vacancyId = savedStateHandle.get<String>(VACANCYID) ?: ""
    private val vacancyDetailsLiveData = MutableLiveData<VacancyDetailsState>(VacancyDetailsState.Loading)
    fun observeVacancyDetailsState(): LiveData<VacancyDetailsState> = vacancyDetailsLiveData

    init {
        if (vacancyId != "") {//значение по умолчанию в nav_graph.xml
            viewModelScope.launch {
                delay(SERVERDELAYTEST)//имитация времени загрузки с сервера
                //тут будет запрос на поиск вакансии согласно id "vacancyId"
                when (TESTTYPE) {
                    0 -> {
                        vacancyDetailsLiveData.postValue(VacancyDetailsState.ServerError)
                    }

                    1 -> {
                        vacancyDetailsLiveData.postValue(VacancyDetailsState.NotFoundError)
                    }

                    else -> {
                        val serversInfo = VacancyDetails(
                            id = vacancyId,
                            name = NAMETEST,
                            company = EMPLNAMETEST,
                            currency = "USD",
                            salaryFrom = 0,
                            salaryTo = null,
                            area = EMPLAREATEST,
                            icon = IMGTEST,
                            experience = EXPTEST,
                            employmentForm = EMPLJOBFORMAT1TEST,
                            workFormat = EMPLJOBFORMAT2TEST,
                            description = DESCRIPTIONTEST,
                            keySkills = testArray
                        )
                        initIsVacancyInFavorite(serversInfo)
                    }
                }
            }
        }
    }

    private suspend fun initIsVacancyInFavorite(vacancyDetails: VacancyDetails) {
        val isLiked = favoriteVacancyInteractor.isVacancyInFavorites(vacancyId)
        vacancyDetailsLiveData.postValue(VacancyDetailsState.VacancyLiked(vacancyDetails, isLiked))
    }

    fun likeButtonControl() {
        val previousState = vacancyDetailsLiveData.value
        if (previousState is VacancyDetailsState.VacancyLiked) {
            val vacancy = Vacancy(
                id = previousState.details.id,
                name = previousState.details.name,
                company = previousState.details.company,
                currency = previousState.details.currency,
                salaryFrom = previousState.details.salaryFrom,
                salaryTo = previousState.details.salaryTo,
                area = previousState.details.area,
                icon = previousState.details.icon
            )
            if (previousState.isLiked == true) {
                deleteVacancyFromFavorites(vacancy)
            } else {
                addVacancyToFavorites(vacancy)
            }
        }
    }

    private fun addVacancyToFavorites(vacancy: Vacancy) {
        viewModelScope.launch {
            val previousState = vacancyDetailsLiveData.value
            if (previousState is VacancyDetailsState.VacancyLiked) {
                favoriteVacancyInteractor.addVacancyToFavorites(vacancy)
                val newLikeStatus = favoriteVacancyInteractor.isVacancyInFavorites(vacancy.id)
                vacancyDetailsLiveData.postValue(VacancyDetailsState.VacancyLiked(previousState.details, newLikeStatus))
            }
        }
    }

    private fun deleteVacancyFromFavorites(vacancy: Vacancy) {
        viewModelScope.launch {
            val previousState = vacancyDetailsLiveData.value
            if (previousState is VacancyDetailsState.VacancyLiked) {
                favoriteVacancyInteractor.deleteVacancyFromFavorites(vacancy.id)
                //переделать, чтобы еще кроме id передать название, фирму, иконку, зарплату, адресс
                val newLikeStatus = favoriteVacancyInteractor.isVacancyInFavorites(vacancy.id)
                vacancyDetailsLiveData.postValue(VacancyDetailsState.VacancyLiked(previousState.details, newLikeStatus))
            }
        }
    }

    fun shareButtonControll() {
        sharingInteractor.openVacancyShare(vacancyId)
    }

    companion object {
        const val VACANCYID = "vacancyId"
        const val SERVERDELAYTEST = 1000L
        const val TESTTYPE =
            2 // 2 и более -без ошибок, 1-ошибка вакансия удалена или нет в базе, 0 -ошибка сервера
        const val NAMETEST = "Хомяк ID"
        const val IMGTEST = "https://hh.ru/employer-logo/289027.png"
        const val EMPLNAMETEST = "ХомякПромПрог"
        const val EMPLAREATEST = "Рай Программистов"
        const val EXPTEST = "От 1 мес опыта"
        const val EMPLJOBFORMAT1TEST = "Полная занятость"
        const val EMPLJOBFORMAT2TEST = "Без графика"
        const val DESCRIPTIONTEST = "стать программистом"
        val testArray = arrayOf(
            "крутить педали",
            "быть хомяком",
            "внизу проверка прокрутки\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n",
            "тест прокрутки"
        )
    }
}
