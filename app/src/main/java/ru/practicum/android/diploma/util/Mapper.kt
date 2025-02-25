package ru.practicum.android.diploma.util

import ru.practicum.android.diploma.search.domain.model.Vacancy
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

