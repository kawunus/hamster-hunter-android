package ru.practicum.android.diploma.search.domain.api

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.model.Vacancy

interface VacanciesSearchRepository {
    val foundCount: Flow<Int?>
    fun searchVacancies(expression: String): Flow<PagingData<Vacancy>>
}
