package ru.practicum.android.diploma.util

import CountryDto
import ru.practicum.android.diploma.filter.data.dto.RegionDTO
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.Region
import ru.practicum.android.diploma.search.domain.model.Vacancy
import ru.practicum.android.diploma.util.Constants.HTTP_NOT_FOUND
import ru.practicum.android.diploma.vacancy.domain.model.ErrorType
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetails

fun VacancyDetails.toVacancy(): Vacancy = Vacancy(
    id = this.id,
    name = this.name,
    company = this.employer,
    currency = this.currency,
    salaryFrom = this.salaryFrom,
    salaryTo = this.salaryTo,
    area = this.area,
    icon = this.icon
)

fun Int.mapToErrorType(): ErrorType {
    return when (this) {
        HTTP_NOT_FOUND -> ErrorType.NOT_FOUND
        else -> ErrorType.UNKNOWN
    }
}

fun CountryDto.toCountry(): Country {
    return Country(
        id = id,
        name = name ?: "Неизвестная страна"
    )
}

fun RegionDTO.toRegion(): Region {
    return Region(
        id = id,
        name = name,
        parentId = parentId
    )
}
