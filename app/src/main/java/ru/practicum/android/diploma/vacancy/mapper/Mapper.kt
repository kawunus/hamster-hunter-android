package ru.practicum.android.diploma.vacancy.mapper

import ru.practicum.android.diploma.vacancy.data.network.model.VacancyByIdResponse
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetails

object Mapper {

    fun VacancyByIdResponse.toVacancyDetails() = VacancyDetails(
        name = name.orEmpty(),
        salaryFrom = salary?.from.toString(),
        salaryTo = salary?.to.toString(),
        employer = employer?.name.orEmpty(),
        area = area?.name.orEmpty(),
        experience = experience?.name.orEmpty(),
        employment = employment?.name.orEmpty(),
        workFormat = workFormat?.map { it.name }.orEmpty(),
        description = description.orEmpty(),
        keySkills = keySkills?.map { it.name }.orEmpty(),
        icon = employer?.logoUrls?.logoUrl.orEmpty()
    )
}
