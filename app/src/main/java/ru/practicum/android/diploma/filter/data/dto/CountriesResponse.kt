package ru.practicum.android.diploma.filter.data.dto

import CountryDto
import ru.practicum.android.diploma.core.data.dto.Response

class CountriesResponse : Response() {
    var countriesList: List<CountryDto> = emptyList()
}
