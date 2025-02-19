package ru.practicum.android.diploma.search.domain.api

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.data.dto.VacancyShortDto

interface VacanciesSearchRepository {
    fun searchVacancies(expression: String): Flow<PagingData<VacancyShortDto>>
}
