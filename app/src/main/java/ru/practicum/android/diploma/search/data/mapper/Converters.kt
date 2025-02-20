package ru.practicum.android.diploma.search.data.mapper

import ru.practicum.android.diploma.search.data.dto.VacancyDto
import ru.practicum.android.diploma.search.domain.model.Vacancy

fun VacancyDto.toDomain(): Vacancy {
    return Vacancy(
        id = id ?: "",
        name = name ?: "",
        company = getEmployerName(),
        currency = getSalaryCurrency(),
        salaryFrom = salary?.from,
        salaryTo = salary?.to,
        area = getAreaName(),
        icon = getEmployerLogoUrl()
    )
}

private fun VacancyDto.getEmployerName(): String {
    return employer?.name ?: ""
}

private fun VacancyDto.getSalaryCurrency(): String {
    return salary?.currency ?: ""
}

private fun VacancyDto.getAreaName(): String {
    return area?.name ?: ""
}

private fun VacancyDto.getEmployerLogoUrl(): String {
    return employer?.logoUrls?.logoUrl ?: ""
}
