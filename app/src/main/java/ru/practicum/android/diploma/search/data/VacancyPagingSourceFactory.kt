package ru.practicum.android.diploma.search.data

import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.core.Koin
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.search.data.network.VacanciesPagingSource
import ru.practicum.android.diploma.search.data.network.model.VacanciesSearchRequest

class VacancyPagingSourceFactory(private val koin: Koin) {
    fun create(searchRequest: VacanciesSearchRequest, foundCount: MutableSharedFlow<Int?>): VacanciesPagingSource {
        return koin.get { parametersOf(searchRequest, foundCount) }
    }
}
