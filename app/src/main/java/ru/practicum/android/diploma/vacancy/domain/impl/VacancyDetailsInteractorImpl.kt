package ru.practicum.android.diploma.vacancy.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsRepository
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetails

class VacancyDetailsInteractorImpl(private val repository: VacancyDetailsRepository) : VacancyDetailsInteractor {
    override fun openVacancyShare(shareUrl: String) {
        repository.openUrlShare(shareUrl)
    }

    override suspend fun findVacancy(vacancyId: Int): Flow<VacancyDetails?> {
        return repository.findVacancyDetails(vacancyId)
    }

}
