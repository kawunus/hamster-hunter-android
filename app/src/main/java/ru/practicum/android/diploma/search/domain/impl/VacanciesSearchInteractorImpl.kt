package ru.practicum.android.diploma.search.domain.impl

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.api.VacanciesSearchInteractor
import ru.practicum.android.diploma.search.domain.api.VacanciesSearchRepository
import ru.practicum.android.diploma.search.domain.model.Vacancy

class VacanciesSearchInteractorImpl(
    private val repository: VacanciesSearchRepository,

    ) : VacanciesSearchInteractor {
    override val foundCount: Flow<Int?> = repository.foundCount
    override fun searchVacancies(expression: String): Flow<PagingData<Vacancy>> {
        return repository.searchVacancies(expression)
    }


}
