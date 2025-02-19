package ru.practicum.android.diploma.search.data.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.getKoin
import ru.practicum.android.diploma.core.network.NetworkClient
import ru.practicum.android.diploma.search.data.dto.VacanciesSearchRequest
import ru.practicum.android.diploma.search.data.dto.VacancyShortDto
import ru.practicum.android.diploma.search.domain.api.VacanciesSearchRepository

class VacanciesSearchRepositoryImpl(private val networkClient: NetworkClient) : VacanciesSearchRepository {

    override fun searchVacancies(expression: String): Flow<PagingData<VacancyShortDto>> {   //TODO изменить возвращаемый тип на Domain-модель
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
                getKoin().get<VacanciesPagingSource> { parametersOf(searchRequest) } // Получаем VacanciesPagingSource через Koin
            }
        ).flow
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}
