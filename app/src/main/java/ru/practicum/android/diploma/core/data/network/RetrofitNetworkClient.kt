package ru.practicum.android.diploma.core.data.network

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.data.network.dto.Response
import ru.practicum.android.diploma.search.data.network.model.VacanciesSearchRequest
import ru.practicum.android.diploma.search.data.network.model.VacanciesSearchResponse
import ru.practicum.android.diploma.util.NetworkMonitor
import ru.practicum.android.diploma.vacancy.data.dto.VacancyByIdRequest

class RetrofitNetworkClient(
    private val context: Context,
    private val hHApiService: HHApiService,
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }
        val token = context.getString(R.string.bearer_token, TOKEN)

        return withContext(Dispatchers.IO) {
            try {
                val response = when (dto) {
                    is VacanciesSearchRequest -> searchVacancies(dto)

                    is VacancyByIdRequest -> hHApiService.getVacancyById(
                        userAgent = USER_AGENT,
                        vacancyId = dto.id,
                    )

                    else -> Response().apply { resultCode = HTTP_BAD_REQUEST }
                }
                response.apply { resultCode = HTTP_SUCCESS }
            } catch (e: HttpException) {
                logError("HTTP", e)
                Response().apply { resultCode = HTTP_SERVER_ERROR }
            }
        }
    }

//    private suspend fun searchVacancies(request: VacanciesSearchRequest): VacanciesSearchResponse {
//        val titleSearchField: String?
//
//        if (request.onlyInTitles == true) {
//            val dictionaryResponse = hHApiService.getDictionaries(userAgent = USER_AGENT)
//            titleSearchField = dictionaryResponse.vacancySearchFields.firstOrNull { it.id == "name" }?.id
//        } else {
//            titleSearchField = null
//        }
//
//        return hHApiService.search(USER_AGENT, request.toQueryMap(titleSearchField))
//    }

    private suspend fun searchVacancies(request: VacanciesSearchRequest): VacanciesSearchResponse {
        val titleSearchField = if (request.onlyInTitles == true) {
            hHApiService.getDictionaries(userAgent = USER_AGENT)
                .vacancySearchFields
                .find { it.id == "name" }
                ?.id
        } else {
            null
        }

        return hHApiService.search(USER_AGENT, request.toQueryMap(titleSearchField))
    }

    private fun logError(errorType: String, e: Exception) {
        Log.d("DEBUG", "$errorType ошибка в методе doRequest: ${e.message}")
    }

    private fun isConnected(): Boolean {
        return NetworkMonitor.isNetworkAvailable(context)
    }

    companion object {
        private const val HTTP_BAD_REQUEST = 400
        private const val HTTP_SERVER_ERROR = 500
        private const val HTTP_SUCCESS = 200
        const val TOKEN = BuildConfig.HH_ACCESS_TOKEN
        const val USER_AGENT =
            "HamsterHunter/1.0 (sergey_sh97@mail.ru)"
    }
}
