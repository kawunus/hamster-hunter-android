package ru.practicum.android.diploma.favorites.util

import ru.practicum.android.diploma.favorites.data.entity.FavoriteVacancyEntity
import ru.practicum.android.diploma.search.domain.model.Vacancy

class FavoriteVacancyConverter {

    fun map(vacancy: Vacancy, addedAt: Long): FavoriteVacancyEntity = FavoriteVacancyEntity(
        id = vacancy.id,
        name = vacancy.name,
        salaryFrom = vacancy.salaryFrom,
        salaryTo = vacancy.salaryTo,
        addedAt = addedAt,
        area = vacancy.area,
        company = vacancy.company,
        currency = vacancy.currency,
        icon = vacancy.icon
    )

    fun map(vacancy: FavoriteVacancyEntity): Vacancy = Vacancy(
        id = vacancy.id,
        name = vacancy.name,
        salaryFrom = vacancy.salaryFrom,
        salaryTo = vacancy.salaryTo,
        area = vacancy.area,
        company = vacancy.company,
        currency = vacancy.currency,
        icon = vacancy.icon
    )

    fun convertListFromVacancyEntity(vacancies: List<FavoriteVacancyEntity>): List<Vacancy> {
        return vacancies.map { track -> map(track) }
    }
}
