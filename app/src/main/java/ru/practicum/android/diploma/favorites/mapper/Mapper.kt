package ru.practicum.android.diploma.favorites.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.favorites.data.entity.FavoriteVacancyEntity
import ru.practicum.android.diploma.favorites.domain.model.FavoritesVacancy

fun FavoritesVacancy.toEntity(addedAt: Long): FavoriteVacancyEntity = FavoriteVacancyEntity(
    id = id,
    name = name,
    salaryFrom = salaryFrom,
    salaryTo = salaryTo,
    addedAt = addedAt,
    currency = currency,
    employer = employer,
    area = area,
    city = city,
    street = street,
    building = building,
    experience = experience,
    employment = employment,
    workFormat = convertToJson(workFormat),
    description = description,
    keySkills = convertToJson(keySkills),
    icon = icon,
    alternateUrl = alternateUrl
)

fun FavoriteVacancyEntity.toDomain(): FavoritesVacancy = FavoritesVacancy(
    id = id,
    name = name,
    salaryFrom = salaryFrom,
    salaryTo = salaryTo,
    addedAt = addedAt,
    currency = currency,
    employer = employer,
    area = area,
    city = city,
    street = street,
    building = building,
    experience = experience,
    employment = employment,
    workFormat = convertFromJson(workFormat),
    description = description,
    keySkills = convertFromJson(keySkills),
    icon = icon,
    alternateUrl = alternateUrl
)

private fun convertToJson(list: List<String>): String = Gson().toJson(list)

private fun convertFromJson(json: String): List<String> {
    val type = object : TypeToken<List<String>>() {}.type
    return Gson().fromJson(json, type)
}
