package ru.practicum.android.diploma.search.data.mapper

import ru.practicum.android.diploma.search.data.dto.VacancyDto
import ru.practicum.android.diploma.search.data.network.model.VacanciesSearchRequest
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
        put("text", text)
        put("page", page.toString())
        area?.let { put("area", it) }
        professionalRole?.let { put("professional_role", it) }
        onlyWithSalary?.let { put("only_with_salary", it.toString()) }

        if (onlyInTitles == true) {
            titleSearchField?.let { put("search_field", it) }
        }
    }
}
