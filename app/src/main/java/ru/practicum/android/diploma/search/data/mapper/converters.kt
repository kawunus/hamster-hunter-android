package ru.practicum.android.diploma.search.data.mapper

import ru.practicum.android.diploma.search.data.dto.VacancyDto
import ru.practicum.android.diploma.search.domain.model.Vacancy

fun VacancyDto.toDomain() = Vacancy(
    id = id,
    name = name,
    company = employer.name ?: "",
    currency = salary.currency ?: "",
    salaryFrom = salary.from,
    salaryTo = salary.to,
    area = area.name ?: "",
    icon = if (employer.logoUrls != null) {
        employer.logoUrls.logoUrl90 ?: ""
    } else ""
)


