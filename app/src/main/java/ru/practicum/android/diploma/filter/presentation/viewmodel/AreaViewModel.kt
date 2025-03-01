package ru.practicum.android.diploma.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.practicum.android.diploma.core.ui.BaseViewModel
import ru.practicum.android.diploma.filter.domain.model.Area
import ru.practicum.android.diploma.filter.domain.model.FilterParameters
import ru.practicum.android.diploma.filter.domain.usecase.FiltersInteractor

class AreaViewModel(private val filtersInteractor: FiltersInteractor) : BaseViewModel() {

    // Храним всю информацию (countryId, regionId, countryName, regionName) в одном объекте
    private val _selectedArea = MutableLiveData<Area?>()
    val selectedArea: LiveData<Area?> = _selectedArea

    init {
        loadFilters()
    }

    // Считываем из SharedPrefs уже сохранённые значения (страна и регион)
    private fun loadFilters() {
        val filters = filtersInteractor.readFilters()
        _selectedArea.value = filters.area
    }

    fun updateArea(newArea: Area) {
        val oldArea = _selectedArea.value
        val isNewCountry = (oldArea?.countryId != newArea.countryId)

        if (isNewCountry) {
            // Пользователь выбрал новую страну – обнуляем регион
            _selectedArea.value = newArea.copy(
                regionId = null,
                regionName = null
            )
        } else {
            // Страна та же – меняем всё, что пришло в newArea (включая regionId/regionName)
            _selectedArea.value = newArea
        }
    }

    fun saveFilters() {
        filtersInteractor.saveFilters(
            FilterParameters(
                area = _selectedArea.value
            )
        )
    }
}
