package ru.practicum.android.diploma.search.data.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.getKoin
import ru.practicum.android.diploma.search.data.network.VacanciesPagingSource
import ru.practicum.android.diploma.search.data.network.model.VacanciesSearchRequest
import ru.practicum.android.diploma.search.domain.api.VacanciesSearchRepository
import ru.practicum.android.diploma.search.domain.model.Vacancy

class VacanciesSearchRepositoryImpl :
    VacanciesSearchRepository {

    private val _foundCount = MutableSharedFlow<Int?>(replay = 0)
    override val foundCount: SharedFlow<Int?> get() = _foundCount

    override fun searchVacancies(expression: String): Flow<PagingData<Vacancy>> {
        //Тут вместо null будем подставлять значения установленных фильтров, если они есть. Если нет - оставляем null.
        val searchRequest = VacanciesSearchRequest(
            text = expression,
            area = null,
            page = 0,
            professionalRole = null,
            onlyWithSalary = null,
            onlyInTitles = null
        )

        return Pager(
            config = PagingConfig(
                enablePlaceholders = false, // Отключение плейсхолдеров
                pageSize = PAGE_SIZE,
                prefetchDistance = PAGE_SIZE / 2
            ),
            pagingSourceFactory = {
                getKoin().get<VacanciesPagingSource> { parametersOf(searchRequest, _foundCount) }
            }
        ).flow
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}
