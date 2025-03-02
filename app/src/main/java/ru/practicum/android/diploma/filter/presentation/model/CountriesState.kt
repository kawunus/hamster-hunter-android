package ru.practicum.android.diploma.filter.presentation.model

import ru.practicum.android.diploma.filter.domain.model.Country

sealed class CountriesState {
    data object Loading : CountriesState()
    data class Success(val countries: List<Country>) : CountriesState()
    data object NetworkError : CountriesState()
    data object ServerError : CountriesState()
}
