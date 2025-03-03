package ru.practicum.android.diploma.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.ui.BaseViewModel
import ru.practicum.android.diploma.filter.domain.model.Area
import ru.practicum.android.diploma.filter.domain.model.FilterParameters
import ru.practicum.android.diploma.filter.domain.model.Industry
import ru.practicum.android.diploma.filter.domain.usecase.FiltersInteractor

class FilterViewModel(private val interactor: FiltersInteractor) : BaseViewModel() {
    private val savedFilters = MutableLiveData(FilterParameters())
    fun getSavedFilters(): LiveData<FilterParameters> = savedFilters

    // для отслеживания первоначальных фильтров и управления видимостью кнопки"Применить"
    private var initialFilters: FilterParameters? = null
    private val filterWasChanged = MutableLiveData(false)
    fun getFilterWasChanged(): LiveData<Boolean> = filterWasChanged
    private var isInitialFiltersSaved = false

    // для управления видимостью кнопки "Сбросить"
    private val anyFilterApplied = MutableLiveData<Boolean?>(null)
    fun getAnyFilterApplied(): LiveData<Boolean?> = anyFilterApplied

    fun checkSavedFilters() {
        viewModelScope.launch {
            val filters = interactor.readFilters()
            savedFilters.value = filters

            if (!isInitialFiltersSaved) { // Сохраняем первоначальное состояние только один раз
                initialFilters = filters.copy()
                isInitialFiltersSaved = true
            }
            checkIfAnyFilterApplied()
            checkIFilterWasChanged()
        }
    }

    private fun updateFilters(update: (FilterParameters) -> FilterParameters) {
        // если сохранённых фильтров ещё не было - создаём обьект FilterParameters null-значениями
        val currentFilters = savedFilters.value ?: FilterParameters()
        val newFilters = update(currentFilters) // применеяем лямбду update к сохранённым ранеее фильтрам
        savedFilters.value = newFilters
        checkIfAnyFilterApplied()
        checkIFilterWasChanged()
        viewModelScope.launch {
            interactor.saveFilters(newFilters)
        }
    }

    fun clearFilters() {
        viewModelScope.launch {
            interactor.clearFilters()
            savedFilters.value = FilterParameters()
            anyFilterApplied.value = false
            checkIFilterWasChanged()
        }
    }

    fun setSalary(salary: Int?) {
        updateFilters { it.copy(salary = salary) }
    }

    fun setArea(area: Area?) {
        updateFilters { it.copy(area = area) }
    }

    fun setIndustry(industry: Industry?) {
        updateFilters { it.copy(industry = industry) }
    }

    fun setOnlyWithSalary(onlyWithSalary: Boolean) {
        updateFilters { it.copy(onlyWithSalary = onlyWithSalary) }
    }

    fun setOnlyInTitles(onlyInTitles: Boolean) {
        updateFilters { it.copy(onlyInTitles = onlyInTitles) }
    }

    private fun checkIFilterWasChanged() {
        filterWasChanged.value = savedFilters.value != initialFilters // Проверяем изменения
    }

    private fun checkIfAnyFilterApplied() {
        savedFilters.value?.let { filters ->
            val parametersList = listOf(
                filters.area,
                filters.industry,
                filters.salary,
            )
            anyFilterApplied.value =
                parametersList.any { it != null } || filters.onlyWithSalary == true || filters.onlyInTitles == true
        }
    }
}
