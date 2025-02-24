package ru.practicum.android.diploma.vacancy.domain.api

import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetails

interface GetVacancyRepository {
    suspend fun execute(vacancyId: Int): VacancyDetails?
}
