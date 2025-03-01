package ru.practicum.android.diploma.search.data.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.getKoin
import ru.practicum.android.diploma.filter.domain.api.FiltersRepository
import ru.practicum.android.diploma.search.data.network.VacanciesPagingSource
import ru.practicum.android.diploma.search.data.network.model.VacanciesSearchRequest
import ru.practicum.android.diploma.search.domain.api.VacanciesSearchRepository
import ru.practicum.android.diploma.search.domain.model.Vacancy

class VacanciesSearchRepositoryImpl(private val filtersRepository: FiltersRepository) :
    VacanciesSearchRepository {

    private val _foundCount = MutableSharedFlow<Int?>(replay = 0)
    override val foundCount: SharedFlow<Int?> get() = _foundCount

    override fun searchVacancies(expression: String): Flow<PagingData<Vacancy>> {
        val filters = filtersRepository.readFilters()
        val searchRequest = VacanciesSearchRequest(
            text = expression,
            page = 0,
            area = filters.area?.regionId ?: filters.area?.countryId,
            salary = filters.salary,
            professionalRole = filters.industry?.id,
            onlyWithSalary = filters.onlyWithSalary,
            onlyInTitles = filters.onlyInTitles,
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

    private companion object {
        const val PAGE_SIZE = 20
    }
}
