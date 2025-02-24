package ru.practicum.android.diploma.vacancy.domain.impl

import ru.practicum.android.diploma.vacancy.domain.api.GetVacancyRepository
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetails
import ru.practicum.android.diploma.vacancy.domain.usecase.GetVacancyUseCase

class GetVacancyUseCaseImpl(
    private val repository: GetVacancyRepository
) : GetVacancyUseCase {
    override suspend fun execute(vacancyId: Int): VacancyDetails? {
        return repository.execute(vacancyId)
    }
}
