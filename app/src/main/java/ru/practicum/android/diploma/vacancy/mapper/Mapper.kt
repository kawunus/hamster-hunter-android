package ru.practicum.android.diploma.vacancy.mapper

import ru.practicum.android.diploma.vacancy.data.network.model.VacancyByIdResponse
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetails

object Mapper {
    fun VacancyByIdResponse.toVacancyDetails() = VacancyDetails(
        id = id.orEmpty(),
        name = name.orEmpty(),
        salaryFrom = salary?.from,
        salaryTo = salary?.to,
        currency = salary?.currency.orEmpty(),
        employer = employer?.name.orEmpty(),
        area = area?.name.orEmpty(),
        city = address?.city.orEmpty(),
        street = address?.street.orEmpty(),
        building = address?.building.orEmpty(),
        experience = experience?.name.orEmpty(),
        employment = employment?.name.orEmpty(),
        workFormat = workFormat?.map { it.name }.orEmpty(),
        description = description.orEmpty(),
        keySkills = keySkills?.map { it.name }.orEmpty(),
        icon = employer?.logoUrls?.logoUrl.orEmpty(),
        alternateUrl = alternateUrl.orEmpty()
    )
}
