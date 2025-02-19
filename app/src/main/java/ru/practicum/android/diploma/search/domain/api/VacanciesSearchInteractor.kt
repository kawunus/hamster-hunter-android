package ru.practicum.android.diploma.search.domain.api

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.model.Vacancy

interface VacanciesSearchInteractor {
    fun searchVacancies(expression: String): Flow<PagingData<Vacancy>>
    suspend fun testSearch(expression: String): List<Vacancy>
}
