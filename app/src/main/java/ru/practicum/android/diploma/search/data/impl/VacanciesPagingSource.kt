package ru.practicum.android.diploma.search.data.impl

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.practicum.android.diploma.core.network.NetworkClient
import ru.practicum.android.diploma.search.data.dto.VacanciesSearchRequest
import ru.practicum.android.diploma.search.data.dto.VacanciesSearchResponse
import ru.practicum.android.diploma.search.data.mapper.toDomain
import ru.practicum.android.diploma.search.presentation.model.Vacancy

class VacanciesPagingSource(
    private val networkClient: NetworkClient,
    private val searchRequest: VacanciesSearchRequest
) : PagingSource<Int, Vacancy>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Vacancy> {
        return try {
            val page = params.key ?: 0
            val updatedRequest = searchRequest.copy(page = page)
            val response = networkClient.doRequest(updatedRequest) as VacanciesSearchResponse

            // Успешная загрузка
            LoadResult.Page(
                data = response.items.map { it.toDomain() },
                prevKey = if (page == 0) null else page - 1, // Предыдущая страница
                nextKey = if (page >= response.pages - 1) null else page + 1 // Следующая страница
            )
        } catch (e: Exception) {
            // Обработка ошибок
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Vacancy>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
