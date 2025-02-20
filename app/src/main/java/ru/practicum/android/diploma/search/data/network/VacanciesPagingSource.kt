package ru.practicum.android.diploma.search.data.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.core.domain.exception.EmptyResultException
import ru.practicum.android.diploma.core.domain.exception.NoInternetException
import ru.practicum.android.diploma.search.data.dto.VacanciesSearchRequest
import ru.practicum.android.diploma.search.data.dto.VacanciesSearchResponse
import ru.practicum.android.diploma.search.data.mapper.toDomain
import ru.practicum.android.diploma.search.domain.model.Vacancy

class VacanciesPagingSource(
    private val networkClient: NetworkClient,
    private val searchRequest: VacanciesSearchRequest,
    private val foundCount: MutableSharedFlow<Int?>
) : PagingSource<Int, Vacancy>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Vacancy> {
        return try {
            val page = params.key ?: 0
            val updatedRequest = searchRequest.copy(page = page)
            val response = networkClient.doRequest(updatedRequest) as VacanciesSearchResponse

            // Обновляем значение foundCount
            foundCount.emit(response.found)

            when (response.resultCode) {
                200 -> {
                    if (response.items.isEmpty()) {
                        LoadResult.Error(EmptyResultException()) // Ошибка "Ничего не найдено"
                    } else {

                        LoadResult.Page(
                            data = response.items.map { it.toDomain() },
                            prevKey = if (page == 0) null else page - 1,
                            nextKey = if (page >= response.pages - 1) null else page + 1
                        )
                    }
                }

                -1 -> LoadResult.Error(NoInternetException()) // Ошибка "Нет интернета"
                else -> LoadResult.Error(Exception("Ошибка сервера: ${response.resultCode}"))
            }
        } catch (e: Exception) {
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


