package ru.practicum.android.diploma.search.data.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.getKoin
import ru.practicum.android.diploma.search.data.dto.VacanciesSearchRequest
import ru.practicum.android.diploma.search.data.network.VacanciesPagingSource
import ru.practicum.android.diploma.search.domain.api.VacanciesSearchRepository
import ru.practicum.android.diploma.search.domain.model.Vacancy

class VacanciesSearchRepositoryImpl() : VacanciesSearchRepository {

    private val _foundCount = MutableSharedFlow<Int?>(replay = 0)
    override val foundCount: SharedFlow<Int?> get() = _foundCount

    override fun searchVacancies(expression: String): Flow<PagingData<Vacancy>> {
        //тут так же нужно будет проверять установленные фильтры и передавать соответствующие значения в VacanciesSearchRequest, пока поставила их просто null
        val searchRequest = VacanciesSearchRequest(
            text = expression,
            page = 0,
            area = null,
            professionalRole = null,
            onlyWithSalary = null
        )

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false // Отключение плейсхолдеров
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
