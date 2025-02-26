package ru.practicum.android.diploma.vacancy.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsRepository
import ru.practicum.android.diploma.vacancy.domain.model.ErrorType
import ru.practicum.android.diploma.vacancy.domain.model.NetworkResult
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetails
import ru.practicum.android.diploma.vacancy.domain.usecase.VacancyDetailsInteractor

class VacancyDetailsInteractorImpl(private val repository: VacancyDetailsRepository) : VacancyDetailsInteractor {
    override fun openVacancyShare(shareUrl: String) {
        repository.openUrlShare(shareUrl)
    }

    override suspend fun findVacancy(vacancyId: String): Flow<Pair<VacancyDetails?, ErrorType?>> {
        return repository.findVacancyDetails(vacancyId).map {
            when (it) {
                is NetworkResult.Success -> Pair(it.data, null)
                is NetworkResult.Error -> Pair(null, it.error)
            }
        }
    }

}
