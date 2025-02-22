package ru.practicum.android.diploma.search.data.network

import android.content.Context
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.MutableSharedFlow
import retrofit2.HttpException
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.core.domain.exception.EmptyResultException
import ru.practicum.android.diploma.core.domain.exception.NoInternetException
import ru.practicum.android.diploma.search.data.mapper.toDomain
import ru.practicum.android.diploma.search.data.network.model.VacanciesSearchRequest
import ru.practicum.android.diploma.search.data.network.model.VacanciesSearchResponse
import ru.practicum.android.diploma.search.domain.model.Vacancy
import ru.practicum.android.diploma.util.NetworkMonitor

class VacanciesPagingSource(
    private val networkClient: NetworkClient,
    private val context: Context,
    private val searchRequest: VacanciesSearchRequest,
    private val foundCount: MutableSharedFlow<Int?>
) : PagingSource<Int, Vacancy>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Vacancy> {
        // Проверяем наличие подключения до выполнения запроса
        if (!isConnected()) {
            return LoadResult.Error(NoInternetException())
        }

        return try {
            val currentPage = params.key ?: 0
            val updatedRequest = searchRequest.copy(page = currentPage)
            val response = networkClient.doRequest(updatedRequest) as VacanciesSearchResponse
            // Обновляем значение foundCount
            foundCount.emit(response.found)

            when (response.resultCode) {
                HTTP_SUCCESS -> {
                    val data = response.items
                    Log.d(
                        "PagingSource",
                        "currentPage: $currentPage, nextKey: ${if (data.isEmpty() || currentPage >= response.pages - 1) null else currentPage + 1}"
                    )
                    if (data.isEmpty()) {
                        LoadResult.Error(EmptyResultException()) // Ошибка "Ничего не найдено"
                    } else {
                        LoadResult.Page(
                            data = data.map { it.toDomain() },
                            prevKey = if (currentPage == 0) null else currentPage - 1,
                            nextKey = if (data.isEmpty() || currentPage >= response.pages - 1) null else currentPage + 1
                        )
                    }
                }

                -1 -> LoadResult.Error(NoInternetException()) // Альтернативная обработка ошибки "Нет интернета"
                else -> LoadResult.Error(Exception("Ошибка сервера: ${response.resultCode}"))
            }
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Vacancy>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.nextKey?.minus(1) ?: anchorPage?.prevKey?.plus(1)
        }
    }

    private fun isConnected(): Boolean {
        return NetworkMonitor.isNetworkAvailable(context)
    }

    companion object {
        private const val HTTP_SUCCESS = 200

    }
}
