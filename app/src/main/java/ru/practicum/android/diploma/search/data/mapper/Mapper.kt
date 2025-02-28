package ru.practicum.android.diploma.search.data.mapper

import ru.practicum.android.diploma.search.data.dto.VacancyDto
import ru.practicum.android.diploma.search.data.network.model.VacanciesSearchRequest
import ru.practicum.android.diploma.search.data.network.model.VacanciesSearchRequest.Companion.AREA
import ru.practicum.android.diploma.search.data.network.model.VacanciesSearchRequest.Companion.ONLY_WITH_SALARY
import ru.practicum.android.diploma.search.data.network.model.VacanciesSearchRequest.Companion.PAGE
import ru.practicum.android.diploma.search.data.network.model.VacanciesSearchRequest.Companion.PROFESSIONAL_ROLE
import ru.practicum.android.diploma.search.data.network.model.VacanciesSearchRequest.Companion.SEARCH_FIELD
import ru.practicum.android.diploma.search.data.network.model.VacanciesSearchRequest.Companion.TEXT
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

fun VacanciesSearchRequest.toQueryMap(
    titleSearchField: String? = null
): Map<String, String> {
    return mutableMapOf<String, String>().apply {
        put(TEXT, text)
        put(PAGE, page.toString())
        area?.let { put(AREA, it) }
        professionalRole?.let { put(PROFESSIONAL_ROLE, it) }
        onlyWithSalary?.let { put(ONLY_WITH_SALARY, it.toString()) }

        if (onlyInTitles == true) {
            titleSearchField?.let { put(SEARCH_FIELD, it) }
        }
    }
}
