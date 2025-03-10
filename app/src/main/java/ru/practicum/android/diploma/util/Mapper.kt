package ru.practicum.android.diploma.util

import ru.practicum.android.diploma.filter.data.dto.CountryDto
import ru.practicum.android.diploma.favorites.domain.model.FavoritesVacancy
import ru.practicum.android.diploma.filter.data.dto.AreaDto
import ru.practicum.android.diploma.filter.data.dto.RegionDto
import ru.practicum.android.diploma.filter.domain.model.Country
import ru.practicum.android.diploma.filter.domain.model.Region
import ru.practicum.android.diploma.search.domain.model.Vacancy
import ru.practicum.android.diploma.util.Constants.HTTP_NOT_FOUND
import ru.practicum.android.diploma.vacancy.domain.model.ErrorType
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetails

fun VacancyDetails.toFavoritesVacancy(): FavoritesVacancy = FavoritesVacancy(
    id = this.id,
    name = this.name,
    salaryFrom = this.salaryFrom,
    salaryTo = this.salaryTo,
    addedAt = System.currentTimeMillis(),
    currency = this.currency,
    employer = this.employer,
    area = this.area,
    city = this.city,
    street = this.street,
    building = this.building,
    experience = this.experience,
    employment = this.employment,
    workFormat = this.workFormat,
    description = this.description,
    keySkills = this.keySkills,
    icon = this.icon,
    alternateUrl = this.alternateUrl
)

fun FavoritesVacancy.toVacancyDetails(): VacancyDetails = VacancyDetails(
    id = this.id,
    name = this.name,
    salaryFrom = this.salaryFrom,
    salaryTo = this.salaryTo,
    currency = this.currency,
    employer = this.employer,
    area = this.area,
    city = this.city,
    street = this.street,
    building = this.building,
    experience = this.experience,
    employment = this.employment,
    workFormat = this.workFormat,
    description = this.description,
    keySkills = this.keySkills,
    icon = this.icon,
    alternateUrl = this.alternateUrl
)

fun FavoritesVacancy.toVacancy(): Vacancy = Vacancy(
    id = this.id,
    name = this.name,
    salaryFrom = this.salaryFrom,
    salaryTo = this.salaryTo,
    currency = this.currency,
    area = this.area,
    company = this.employer,
    icon = this.icon
)

fun Int.mapToErrorType(): ErrorType {
    return when (this) {
        HTTP_NOT_FOUND -> ErrorType.NOT_FOUND
        -1 -> ErrorType.NO_NETWORK
        else -> ErrorType.UNKNOWN
    }
}

fun CountryDto.toCountry(): Country {
    return Country(
        id = id,
        name = name ?: "Неизвестная страна"
    )
}

fun RegionDto.toRegion(): Region {
    return Region(
        id = id,
        name = name ?: "Неизвестный регион",
        parentId = parentId
    )
}

fun AreaDto.toCountryDto(): CountryDto {
    return CountryDto(
        id = id,
        parentId = parentId,
        name = name
    )
}

