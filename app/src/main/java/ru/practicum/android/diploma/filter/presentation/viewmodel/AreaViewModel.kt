package ru.practicum.android.diploma.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.practicum.android.diploma.core.ui.BaseViewModel
import ru.practicum.android.diploma.filter.domain.model.Area
import ru.practicum.android.diploma.filter.domain.model.FilterParameters
import ru.practicum.android.diploma.filter.domain.usecase.FiltersInteractor

class AreaViewModel(private val filtersInteractor: FiltersInteractor) : BaseViewModel() {

    private val _selectedArea = MutableLiveData<Area?>()
    val selectedArea: LiveData<Area?> = _selectedArea

    private val _selectedRegion = MutableLiveData<String?>()
    val selectedRegion: LiveData<String?> = _selectedRegion

    init {
        loadFilters()
    }

    private fun loadFilters() {
        val filters = filtersInteractor.readFilters()
        _selectedArea.value = filters.area
        _selectedRegion.value = filters.area?.id
    }

    fun updateArea(area: Area) {
        _selectedArea.value = area
        _selectedRegion.value = null
    }

    fun updateRegion(region: String) {
        _selectedRegion.value = region
    }

    fun saveFilters() {
        filtersInteractor.saveFilters(
            FilterParameters(
                area = _selectedArea.value?.copy(id = _selectedRegion.value)
            )
        )
    }
}

