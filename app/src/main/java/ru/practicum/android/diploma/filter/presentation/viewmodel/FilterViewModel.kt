package ru.practicum.android.diploma.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.ui.BaseViewModel
import ru.practicum.android.diploma.filter.domain.model.FilterParameters
import ru.practicum.android.diploma.filter.domain.usecase.FiltersInteractor

class FilterViewModel(private val interactor: FiltersInteractor) : BaseViewModel() {
    init {
        checkSavedFilters() // получаем свежие данные о сохранённых фильтрах при инициализации ViewModel
    }
    // обязательно дополнительно в OnResume обновлять данные  (checkSavedFilters)

    private val savedFilters = MutableLiveData(FilterParameters())
    fun getSavedFilters(): LiveData<FilterParameters> = savedFilters

    private val filterWasChanged = MutableLiveData(false)
    fun getFilterWasChanged(): LiveData<Boolean> =
        filterWasChanged // для управления видимостью кнопки "Gрименить"

    private val anyFilterApplied = MutableLiveData<Boolean?>(null) // для управления видимостью кнопки "Сбросить"
    fun getAnyFilterApplied(): LiveData<Boolean?> = anyFilterApplied

    fun checkSavedFilters() {
        viewModelScope.launch {
            savedFilters.value = interactor.readFilters()
        }
    }

    private fun updateFilters(update: (FilterParameters) -> FilterParameters) {
        val currentFilters = savedFilters.value
            ?: FilterParameters() // если сохранённых фильтров ещё не было - создаём обьект FilterParameters null-значениями
        val newFilters = update(currentFilters) // применеяем лямбду update к сохранённым ранеее фильтрам
        savedFilters.value = newFilters

        viewModelScope.launch {
            interactor.saveFilters(newFilters)
        }  // сохраняем новые значения в корутине, чтобы не вызывать задержку интерфейса
    }

    fun clearFilters() {
        interactor.clearFilters()
        savedFilters.value = FilterParameters()
    }

    fun setSalary(salary: Int?) {
        updateFilters { it.copy(salary = salary) }
    }

    fun setOnlyWithSalary(onlyWithSalary: Boolean) {
        updateFilters { it.copy(onlyWithSalary = onlyWithSalary) }
    }

    fun setOnlyInTitles(onlyInTitles: Boolean) {
        updateFilters { it.copy(onlyInTitles = onlyInTitles) }
    }

    fun checkIfAnyFilterApplied() {
        val filters = savedFilters.value
        with(filters) {
            val parametersList = listOf(area, professionalRole, salary, onlyWithSalary, onlyInTitles)
            anyFilterApplied.value = parametersList.any { it != null }
        }
    }
}

// Если в приложении есть сохранённые непустые настройки параметров фильтрации, то кнопка фильтра на главном экране находится в подсвеченном состоянии.

// Кнопка «Применить» появляется, если пользователь указал фильтр, отличающийся от предыдущего.
