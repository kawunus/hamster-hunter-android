package ru.practicum.android.diploma.filter.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.BaseViewModel
import ru.practicum.android.diploma.filter.domain.model.Area
import ru.practicum.android.diploma.filter.domain.model.Country
import ru.practicum.android.diploma.filter.domain.usecase.FiltersInteractor
import ru.practicum.android.diploma.filter.domain.usecase.GetCountriesUseCase
import ru.practicum.android.diploma.filter.presentation.model.CountriesState
import ru.practicum.android.diploma.util.Constants

class CountryViewModel(
    private val getCountriesUseCase: GetCountriesUseCase,
    private val filtersInteractor: FiltersInteractor
) : BaseViewModel() {

    private val _uiState = MutableLiveData<CountriesState>()
    val uiState: LiveData<CountriesState> = _uiState

    fun loadCountries() {
        _uiState.value = CountriesState.Loading

        viewModelScope.launch {
            getCountriesUseCase.getCountries()
                .catch { throwable ->
                    Log.e(
                        "CountriesSearch",
                        "Непредвиденная ошибка или IOException: ${throwable.localizedMessage}",
                        throwable
                    )
                    _uiState.value = CountriesState.NetworkError(R.string.error_no_internet.toString())
                }
                .collect { resource ->
                    when (resource.code) {
                        Constants.HTTP_SUCCESS -> {
                            val list = resource.data ?: emptyList()
                            _uiState.value = CountriesState.Success(list)
                        }

                        Constants.HTTP_NOT_FOUND -> {
                            _uiState.value = CountriesState.ServerError(R.string.error_server.toString())
                        }

                        -1 -> {
                            _uiState.value = CountriesState.NetworkError(R.string.error_no_internet.toString())
                        }

                        else -> {
                            _uiState.value = CountriesState.ServerError(R.string.error_server.toString())
                        }
                    }
                }
        }
    }

    fun onCountrySelected(country: Country) {
        val currentFilters = filtersInteractor.readFilters()

        val newArea = Area(
            country = country,
            region = if (currentFilters.area?.country?.id.equals(country.name)) {
                currentFilters.area?.region
            } else {
                null
            }
        )
        val updatedFilters = currentFilters.copy(area = newArea)

        filtersInteractor.saveFilters(updatedFilters)
    }
}
