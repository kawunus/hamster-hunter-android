package ru.practicum.android.diploma.vacancy.domain.usecase

import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetails

interface GetVacancyUseCase {
    suspend fun execute(vacancyId: Int): VacancyDetails?
}
