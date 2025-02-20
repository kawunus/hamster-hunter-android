package ru.practicum.android.diploma.search.data.mapper

import ru.practicum.android.diploma.search.data.dto.VacancyDto
import ru.practicum.android.diploma.search.domain.model.Vacancy

fun VacancyDto.toDomain(): Vacancy {
    return Vacancy(
        id = id ?: "",
        name = name ?: "",
        company = if (employer != null) {
            employer.name ?: ""
        } else "",
        currency = if (salary != null) {
            salary.currency ?: ""
        } else "",
        salaryFrom = salary?.from,
        salaryTo = salary?.to,
        area = if (area != null) {
            area.name ?: ""
        } else "",
        icon = if (employer?.logoUrls != null) {
            employer.logoUrls.logoUrl ?: ""
        } else ""
    )
}


