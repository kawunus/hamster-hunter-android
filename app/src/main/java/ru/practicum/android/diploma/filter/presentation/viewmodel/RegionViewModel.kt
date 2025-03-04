package ru.practicum.android.diploma.filter.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.ui.BaseViewModel
import ru.practicum.android.diploma.filter.domain.model.Area
import ru.practicum.android.diploma.filter.domain.model.Region
import ru.practicum.android.diploma.filter.domain.usecase.FiltersInteractor
import ru.practicum.android.diploma.filter.domain.usecase.GetRegionsInteractor
import ru.practicum.android.diploma.filter.presentation.model.RegionScreenState
import ru.practicum.android.diploma.util.Constants

class RegionViewModel(
    private val getRegionsInteractor: GetRegionsInteractor,
    private val filtersInteractor: FiltersInteractor
) : BaseViewModel() {

    private val _regions = MutableStateFlow<List<Region>>(emptyList())

    private val _screenState = MutableStateFlow<RegionScreenState>(RegionScreenState.Loading)
    val screenState = _screenState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val filteredRegions = combine(_regions, _searchQuery) { regions, query ->
        if (query.isEmpty()) {
            regions
        } else {
            regions.filter { it.name?.contains(query, ignoreCase = true) == true }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptyList()
    )

    fun getParentId(): String? {
        return filtersInteractor.readTempFilters().area?.country?.id
            ?: filtersInteractor.readFilters().area?.country?.id
    }

    fun loadRegions(countryId: String) {
        viewModelScope.launch {
            _screenState.value = RegionScreenState.Loading
            val regionsFlow = if (countryId.isEmpty()) {
                getRegionsInteractor.getAllRegions()
            } else {
                getRegionsInteractor.getRegions(countryId)
            }
            regionsFlow.collect { resource ->
                Log.e("RegionSearch", "Код ответа: ${resource.code}")
                _screenState.value = when (resource.code) {
                    Constants.HTTP_SUCCESS -> {
                        val regions = resource.data?.sortedBy { it.name } ?: emptyList()
                        _regions.value = regions
                        RegionScreenState.Content
                    }

                    -1 -> {
                        _regions.value = emptyList()
                        RegionScreenState.Error.NetworkError
                    }

                    else -> {
                        _regions.value = emptyList()
                        RegionScreenState.Error.ServerError
                    }
                }
            }
        }
    }

    fun saveSelectedRegion(region: Region) {
        val currentFilters = filtersInteractor.readTempFilters()
        val country = currentFilters.area?.country
        val updatedFilters = currentFilters.copy(
            area = Area(
                region = region,
                country = country
            )
        )
        filtersInteractor.saveTempFilters(updatedFilters)
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}
