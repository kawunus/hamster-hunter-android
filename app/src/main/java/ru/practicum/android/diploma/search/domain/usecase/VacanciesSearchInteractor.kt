package ru.practicum.android.diploma.search.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.model.Vacancy

interface VacanciesSearchInteractor {
    val foundCount: Flow<Int?>
    fun searchVacancies(expression: String): Flow<PagingData<Vacancy>>
}
