package ru.practicum.android.diploma.favorites.mapper

import ru.practicum.android.diploma.favorites.data.entity.FavoriteVacancyEntity
import ru.practicum.android.diploma.favorites.domain.model.FavoriteVacancy

fun FavoriteVacancy.toEntity(addedAt: Long): FavoriteVacancyEntity = FavoriteVacancyEntity(
    id = id,
    name = name,
    salaryFrom = salaryFrom,
    salaryTo = salaryTo,
    addedAt = addedAt,
    area = area,
    company = company,
    currency = currency,
    icon = icon
)

fun FavoriteVacancyEntity.toDomain(): FavoriteVacancy = FavoriteVacancy(
    id = id,
    name = name,
    salaryFrom = salaryFrom,
    salaryTo = salaryTo,
    area = area,
    company = company,
    currency = currency,
    icon = icon
)
