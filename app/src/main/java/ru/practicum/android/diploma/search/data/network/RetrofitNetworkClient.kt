package ru.practicum.android.diploma.search.data.network

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.search.data.dto.Response
import ru.practicum.android.diploma.search.data.dto.VacanciesSearchRequest
import ru.practicum.android.diploma.search.data.dto.VacancyByIdRequest
import ru.practicum.android.diploma.util.NetworkMonitor

class RetrofitNetworkClient(
    private val context: Context,
    private val hHApiService: HHApiService,
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }
        return withContext(Dispatchers.IO) {
            val userAgent = USER_AGENT
            val token = "Bearer $TOKEN"

            try {
                val response = when (dto) {
                    is VacanciesSearchRequest -> hHApiService.search(
                        userAgent = userAgent, token = token, body = dto,
                    )

                    is VacancyByIdRequest -> hHApiService.getVacancyById(
                        userAgent = userAgent,
                        token = token,
                        vacancyId = dto.id,
                    )

                    else -> Response().apply { resultCode = 400 }
                }
                response.apply { resultCode = 200 }
            } catch (e: Throwable) {
                Response().apply { resultCode = 500 }
            }
        }
    }


    private fun isConnected(): Boolean {
        return NetworkMonitor.isNetworkAvailable(context)
    }

    companion object {
        const val TOKEN = BuildConfig.HH_ACCESS_TOKEN
        const val USER_AGENT = "HamsterHunter/1.0 (s.rubinets@gmail.com)"
    }
}
