package ru.practicum.android.diploma.vacancy.mapper

import ru.practicum.android.diploma.vacancy.data.network.model.VacancyByIdResponse
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetails

object Mapper {

    fun VacancyByIdResponse.toVacancyDetails() = VacancyDetails(
        name = name,
        salaryFrom = salary?.from.toString(),
        salaryTo = salary?.to.toString(),
        employer = employer.name.orEmpty(),
        area = area.name,
        experience = experience.name,
        employment = employment.name,
        workFormat = workFormat.map { it.name },
        description = description,
        keySkills = keySkills.map { it.name },
        icon = employer.logoUrls?.logoUrl.orEmpty()
    )
}
