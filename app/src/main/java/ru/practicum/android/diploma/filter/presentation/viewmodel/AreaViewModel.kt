package ru.practicum.android.diploma.filter.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.ui.BaseViewModel
import ru.practicum.android.diploma.filter.domain.model.Area
import ru.practicum.android.diploma.filter.domain.model.FilterParameters
import ru.practicum.android.diploma.filter.domain.usecase.FiltersInteractor
import ru.practicum.android.diploma.filter.domain.usecase.GetCountriesUseCase

class AreaViewModel(
    private val filtersInteractor: FiltersInteractor,
    private val getCountriesUseCase: GetCountriesUseCase
) : BaseViewModel() {

    private val _selectedArea = MutableLiveData<Area?>()
    val selectedArea: LiveData<Area?> = _selectedArea
    private val _isAcceptable = MutableLiveData(true)
    val isAcceptable: LiveData<Boolean> = _isAcceptable

    init {
        loadFilters()
    }

    fun getActualFilterState() {
        loadFilters()
    }

    private fun loadFilters() {
        val tempFilters = filtersInteractor.readTempFilters().area
        _selectedArea.value = tempFilters ?: filtersInteractor.readFilters().area
        saveTempFilters()
    }

    fun updateArea(newArea: Area) {
        val oldArea = _selectedArea.value
        val isNewCountry = oldArea?.country?.id != newArea.country?.id

        if (isNewCountry) {
            _selectedArea.value = newArea.copy(
                region = null
            )
        } else {
            _selectedArea.value = newArea
        }

        saveTempFilters()
    }

    fun saveFilters() {
        val currentFilters = filtersInteractor.readFilters()
        filtersInteractor.saveFilters(
            currentFilters.copy(
                area = _selectedArea.value
            )
        )
        clearTempFilters()
    }

    private fun saveTempFilters() {
        val currentFilters = filtersInteractor.readTempFilters()
        filtersInteractor.saveTempFilters(
            currentFilters.copy(
                area = _selectedArea.value
            )
        )
    }

    fun clearTempFilters() {
        filtersInteractor.saveTempFilters(FilterParameters(area = null))
    }

    fun areFiltersEqual(countryName: String?, regionName: String?) {
        var country = countryName
        var region = regionName

        if (countryName.equals("")) {
            country = null
        }

        if (regionName.equals("")) {
            region = null
        }

        val equal = (filtersInteractor.readFilters().area?.country?.name == country) and
            (filtersInteractor.readFilters().area?.region?.name == region)

        _isAcceptable.postValue(equal)
    }

    fun getCountryByRegion() {
        viewModelScope.launch {
            getCountriesUseCase.getCountries()
                .catch { throwable ->
                    Log.e(
                        "CountriesSearch",
                        "Непредвиденная ошибка или IOException: ${throwable.localizedMessage}",
                        throwable
                    )
                }
                .collect { resource ->
                    val list = resource.data ?: emptyList()
                    val country = list.find {
                        it.id == selectedArea.value?.region?.parentId
                    }
                    val currentArea = selectedArea.value
                    _selectedArea.value = currentArea?.copy(country = country)
                }
        }
    }
}
