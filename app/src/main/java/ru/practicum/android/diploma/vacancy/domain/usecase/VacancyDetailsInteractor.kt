package ru.practicum.android.diploma.vacancy.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.vacancy.domain.model.ErrorType
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetails

interface VacancyDetailsInteractor {

    fun openVacancyShare(shareUrl: String)

    suspend fun findVacancy(vacancyId: Int): Flow<Pair<VacancyDetails?, ErrorType?>>

}
