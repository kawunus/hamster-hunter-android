package ru.practicum.android.diploma.vacancy.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetails

interface VacancyDetailsRepository {
    fun openUrlShare(shareUrl: String)
    suspend fun findVacancyDetails(vacancyId: Int): Flow<VacancyDetails?>
}
