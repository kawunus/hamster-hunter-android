package ru.practicum.android.diploma.search.data.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import ru.practicum.android.diploma.search.data.VacancyPagingSourceFactory
import ru.practicum.android.diploma.search.data.network.model.VacanciesSearchRequest
import ru.practicum.android.diploma.search.domain.api.VacanciesSearchRepository
import ru.practicum.android.diploma.search.domain.model.Vacancy

class VacanciesSearchRepositoryImpl(private val pagingSourceFactory: VacancyPagingSourceFactory) :
    VacanciesSearchRepository {

    private val _foundCount = MutableSharedFlow<Int?>(replay = 0)
    override val foundCount: SharedFlow<Int?> get() = _foundCount

    override fun searchVacancies(expression: String): Flow<PagingData<Vacancy>> {
        val searchRequest = VacanciesSearchRequest(
            text = expression,
            page = 0,
            area = null,
            professionalRole = null,
            onlyWithSalary = null
        )

        return Pager(
            config = PagingConfig(
                enablePlaceholders = true,
                pageSize = PAGE_SIZE,
                prefetchDistance = PAGE_SIZE / 2
            ),
            pagingSourceFactory = { pagingSourceFactory.create(searchRequest, _foundCount) }
        ).flow
    }

//        return Pager(
//            config = PagingConfig(
//                enablePlaceholders = true, // Отключение плейсхолдеров
//                pageSize = PAGE_SIZE,
//                prefetchDistance = PAGE_SIZE / 2
//            ),
//            pagingSourceFactory = {
//                getKoin().get<VacanciesPagingSource> { parametersOf(searchRequest, _foundCount) }
//            }
//        ).flow

    companion object {
        const val PAGE_SIZE = 20
    }
}
