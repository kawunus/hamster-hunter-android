package ru.practicum.android.diploma.util

import ru.practicum.android.diploma.favorites.domain.model.FavoriteVacancy
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetails

fun VacancyDetails.toFavoriteVacancy(): FavoriteVacancy {
    return FavoriteVacancy(
        id = this.id,
        name = this.name,
        company = this.company,
        currency = this.currency,
        salaryFrom = this.salaryFrom,
        salaryTo = this.salaryTo,
        area = this.area,
        icon = this.icon
    )
}
