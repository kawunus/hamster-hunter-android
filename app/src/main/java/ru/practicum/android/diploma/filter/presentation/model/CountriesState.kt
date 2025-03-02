package ru.practicum.android.diploma.filter.presentation.model

import ru.practicum.android.diploma.filter.domain.model.Country

sealed class CountriesState {
    object Loading : CountriesState()
    data class Success(val countries: List<Country>) : CountriesState()
    data class NetworkError(val message: String) : CountriesState()
    data class ServerError(val message: String) : CountriesState()
}
