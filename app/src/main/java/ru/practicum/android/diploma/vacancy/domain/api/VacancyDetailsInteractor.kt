package ru.practicum.android.diploma.vacancy.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetails

interface VacancyDetailsInteractor {
    fun openVacancyShare(vacancyId: String)
    suspend fun findVacancy(vacancyId: Int): Flow<VacancyDetails?>
}
