package ru.practicum.android.diploma.vacancy.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.ui.BaseViewModel
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetails
import ru.practicum.android.diploma.vacancy.domain.usecase.GetVacancyUseCase

class VacancyViewModel(private val getVacancyUseCase: GetVacancyUseCase) : BaseViewModel() {

    private val vacancyLiveData = MutableLiveData<VacancyDetails>()

    val observeVacancy: LiveData<VacancyDetails> = vacancyLiveData

    fun getVacancy(vacancyId: Int) {
        viewModelScope.launch {
            vacancyLiveData.value = getVacancyUseCase.execute(vacancyId)
        }
    }
}
