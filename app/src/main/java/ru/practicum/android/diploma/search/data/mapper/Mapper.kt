package ru.practicum.android.diploma.search.data.mapper

import ru.practicum.android.diploma.search.data.dto.VacancyDto
import ru.practicum.android.diploma.search.data.network.model.VacanciesSearchRequest
import ru.practicum.android.diploma.search.data.network.model.VacanciesSearchRequest.Companion.AREA
import ru.practicum.android.diploma.search.data.network.model.VacanciesSearchRequest.Companion.INDUSTRY
import ru.practicum.android.diploma.search.data.network.model.VacanciesSearchRequest.Companion.ONLY_WITH_SALARY
import ru.practicum.android.diploma.search.data.network.model.VacanciesSearchRequest.Companion.PAGE
import ru.practicum.android.diploma.search.data.network.model.VacanciesSearchRequest.Companion.SALARY
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
    return mapOf(
        TEXT to text,
        PAGE to page.toString(),
        AREA to area,
        INDUSTRY to industry,
        SALARY to salary?.toString(),
        ONLY_WITH_SALARY to onlyWithSalary?.toString(),
        SEARCH_FIELD to titleSearchField.takeIf { onlyInTitles == true }
    ).filterValues { it != null }
        .mapValues { it.value!! } // Безопасно преобразуем String? -> String
}
