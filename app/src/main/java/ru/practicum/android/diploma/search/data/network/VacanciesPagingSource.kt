package ru.practicum.android.diploma.search.data.network

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.MutableSharedFlow
import retrofit2.HttpException
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.core.data.network.exception.EmptyResultException
import ru.practicum.android.diploma.core.data.network.exception.NoInternetException
import ru.practicum.android.diploma.search.data.dto.VacancyDto
import ru.practicum.android.diploma.search.data.mapper.toDomain
import ru.practicum.android.diploma.search.data.network.model.VacanciesSearchRequest
import ru.practicum.android.diploma.search.data.network.model.VacanciesSearchResponse
import ru.practicum.android.diploma.search.domain.model.Vacancy
import ru.practicum.android.diploma.util.Constants.HTTP_SUCCESS
import ru.practicum.android.diploma.util.NetworkMonitor
import java.io.IOException
import java.net.SocketTimeoutException

class VacanciesPagingSource(
    private val networkClient: NetworkClient,
    private val context: Context,
    private val searchRequest: VacanciesSearchRequest,
    private val foundCount: MutableSharedFlow<Int?>
) : PagingSource<Int, Vacancy>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Vacancy> {
        if (!isConnected()) return LoadResult.Error(NoInternetException())

        return runCatching {
            val currentPage = params.key ?: 0
            val response = fetchVacancies(currentPage)

            if (currentPage == 0) foundCount.emit(response.found)

            processResponse(response, currentPage)
        }.getOrElse { handleException(it) }
    }

    private suspend fun fetchVacancies(page: Int): VacanciesSearchResponse {
        val updatedRequest = searchRequest.copy(page = page)
        return networkClient.doRequest(updatedRequest) as VacanciesSearchResponse
    }

    private fun processResponse(response: VacanciesSearchResponse, page: Int): LoadResult<Int, Vacancy> {
        return when (response.resultCode) {
            HTTP_SUCCESS -> createPage(response.items, page, response.pages)
            -1 -> LoadResult.Error(NoInternetException())
            else -> LoadResult.Error(Exception("Ошибка сервера: ${response.resultCode}"))
        }
    }

    private fun createPage(data: List<VacancyDto>, page: Int, totalPages: Int): LoadResult<Int, Vacancy> {
        return if (data.isEmpty()) {
            LoadResult.Error(EmptyResultException())
        } else {
            LoadResult.Page(
                data = data.map { it.toDomain() },
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (page >= totalPages - 1) null else page + 1
            )
        }
    }

    private fun handleException(e: Throwable): LoadResult<Int, Vacancy> {
        return when (e) {
            is HttpException, is SocketTimeoutException, is IOException -> LoadResult.Error(e)
            else -> throw e
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
}
