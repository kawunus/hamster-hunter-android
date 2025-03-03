package ru.practicum.android.diploma.filter.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.ui.BaseViewModel
import ru.practicum.android.diploma.filter.domain.model.Area
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

    fun loadIndustries() {
        _uiState.value = IndustriesState.Loading

        viewModelScope.launch {
            getIndustriesUseCase.getAllIndustries().catch { throwable ->
                Log.e(
                    "IndustriesSearch",
                    "Непредвиденная ошибка или IOException: ${throwable.localizedMessage}",
                    throwable
                )
                _uiState.value = IndustriesState.NetworkError
            }.collect { resource ->
                when (resource.code) {
                    Constants.HTTP_SUCCESS -> {
                        val list = resource.data ?: emptyList()
                        _uiState.value = IndustriesState.Success(list)
                    }

                    Constants.HTTP_NOT_FOUND -> {
                        _uiState.value = IndustriesState.ServerError
                    }

                    -1 -> {
                        _uiState.value = IndustriesState.NetworkError
                    }

                    else -> {
                        _uiState.value = IndustriesState.ServerError
                    }
                }
            }
        }
    }

    fun selectIndustry(industry: Industry) {
        _selectedIndustry.value = industry
    }

    fun saveSelectedIndustryToFilters() {
        val currentFilters = filtersInteractor.readFilters()
        val updatedFilters = currentFilters.copy(
            industry = _selectedIndustry.value
        )
        filtersInteractor.saveFilters(updatedFilters)
    }
}
