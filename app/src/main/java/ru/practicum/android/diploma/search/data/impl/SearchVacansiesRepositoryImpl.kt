package ru.practicum.android.diploma.search.data.impl

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.getKoin
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.search.data.dto.VacanciesSearchRequest
import ru.practicum.android.diploma.search.data.dto.VacanciesSearchResponse
import ru.practicum.android.diploma.search.data.mapper.toDomain
import ru.practicum.android.diploma.search.domain.api.VacanciesSearchRepository
import ru.practicum.android.diploma.search.domain.model.Vacancy

class VacanciesSearchRepositoryImpl(private val networkClient: NetworkClient) : VacanciesSearchRepository {

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
                getKoin().get<VacanciesPagingSource> { parametersOf(searchRequest) }
            }
        ).flow

    }

    override suspend fun testSearch(expression: String): List<Vacancy> {
        val searchRequest = VacanciesSearchRequest(
            text = expression,
            page = 0,
            area = null,
            professionalRole = null,
            onlyWithSalary = null
        )
        val response = networkClient.doRequest(searchRequest)
        if (response is VacanciesSearchResponse) {
            val vacancies = response.items.map { it.toDomain() }
            Log.d("DEBUG", "Вызов testSearch в VacanciesSearchRepositoryImpl, результат: $vacancies")
            return vacancies
        } else {
            Log.d("DEBUG", "Ответ - не VacanciesSearchResponse. КОд ответа: ${response.resultCode}")
            return listOf()
        }
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}
