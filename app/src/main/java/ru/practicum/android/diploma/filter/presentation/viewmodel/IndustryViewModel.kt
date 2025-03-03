package ru.practicum.android.diploma.filter.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.ui.BaseViewModel
import ru.practicum.android.diploma.filter.domain.model.Industry
import ru.practicum.android.diploma.filter.domain.usecase.FiltersInteractor
import ru.practicum.android.diploma.filter.domain.usecase.GetIndustriesUseCase
import ru.practicum.android.diploma.filter.presentation.model.IndustriesState
import ru.practicum.android.diploma.util.Constants

class IndustryViewModel(
    private val getIndustriesUseCase: GetIndustriesUseCase,
    private val filtersInteractor: FiltersInteractor
) : BaseViewModel() {

    private val _uiState = MutableLiveData<IndustriesState>()
    val uiState: LiveData<IndustriesState> = _uiState

    private val _selectedIndustry = MutableLiveData<Industry?>(null)
    val selectedIndustry: LiveData<Industry?> = _selectedIndustry

    private val allIndustries = mutableListOf<Industry>() // Полный список отраслей
    private val _filteredIndustries = MutableLiveData<List<Industry>>() // Отфильтрованный список
    val filteredIndustries: LiveData<List<Industry>> = _filteredIndustries

    private var currentSearchQuery: String = ""

    fun loadIndustries() {
        _uiState.value = IndustriesState.Loading
        loadSelectedIndustry()

        viewModelScope.launch {
            getIndustriesUseCase.getAllIndustries().catch { throwable ->
                Log.e("IndustriesSearch", "Ошибка загрузки: ${throwable.localizedMessage}", throwable)
                _uiState.value = IndustriesState.NetworkError
            }.collect { resource ->
                when (resource.code) {
                    Constants.HTTP_SUCCESS -> {
                        val list = resource.data ?: emptyList()
                        allIndustries.clear()
                        allIndustries.addAll(list)
                        _filteredIndustries.value = list
                        _uiState.value = IndustriesState.Success(list)
                    }
                    Constants.HTTP_NOT_FOUND, -1 -> _uiState.value = IndustriesState.NetworkError
                    else -> _uiState.value = IndustriesState.ServerError
                }
            }
        }
    }



    fun selectIndustry(industry: Industry) {
        _selectedIndustry.value = industry
        saveSelectedIndustryToFilters()
    }

    fun saveSelectedIndustryToFilters() {
        val currentFilters = filtersInteractor.readFilters()
        val updatedFilters = currentFilters.copy(industry = _selectedIndustry.value)
        filtersInteractor.saveFilters(updatedFilters)
    }

    fun updateSearchQuery(query: String) {
        currentSearchQuery = query.trim().lowercase()

        _filteredIndustries.value = if (currentSearchQuery.isEmpty()) {
            allIndustries
        } else {
            allIndustries.filter { it.name!!.lowercase().contains(currentSearchQuery) }
        }
    }

    fun loadSelectedIndustry() {
        _selectedIndustry.value = filtersInteractor.readFilters().industry
    }

}

