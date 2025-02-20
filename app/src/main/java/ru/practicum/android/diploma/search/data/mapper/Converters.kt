package ru.practicum.android.diploma.search.data.mapper

import ru.practicum.android.diploma.search.data.dto.VacancyDto
import ru.practicum.android.diploma.search.domain.model.Vacancy

fun VacancyDto.toDomain(): Vacancy {
    return Vacancy(
        id = id ?: "",
        name = name ?: "",
        company = employer?.name ?: "",
        currency = salary?.currency ?: "",
        salaryFrom = salary?.from,
        salaryTo = salary?.to,
        area = area?.name ?: "",
        icon = employer?.logoUrls?.logoUrl ?: ""
    )
}
