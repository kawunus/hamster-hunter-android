package ru.practicum.android.diploma.search.data.network

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.MutableSharedFlow
import retrofit2.HttpException
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.core.data.network.exception.EmptyResultException
import ru.practicum.android.diploma.core.data.network.exception.NoInternetException
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
        if (!isConnected()) { // Проверяем наличие подключения до выполнения запроса
            return LoadResult.Error(NoInternetException())
        }

        return try {
            val currentPage = params.key ?: 0
            val updatedRequest = searchRequest.copy(page = currentPage)
            val response = networkClient.doRequest(updatedRequest) as VacanciesSearchResponse

            if (currentPage == 0) {
                foundCount.emit(response.found) // Обновляем значение foundCount при загрузке первой страницы
            }

            when (response.resultCode) {
                HTTP_SUCCESS -> {
                    val data = response.items
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
