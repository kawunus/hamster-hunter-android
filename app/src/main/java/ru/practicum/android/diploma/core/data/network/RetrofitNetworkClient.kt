package ru.practicum.android.diploma.core.data.network

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.core.data.network.dto.Response
import ru.practicum.android.diploma.search.data.dto.VacanciesSearchRequest
import ru.practicum.android.diploma.util.NetworkMonitor
import ru.practicum.android.diploma.vacancy.data.dto.VacancyByIdRequest

class RetrofitNetworkClient(
    private val context: Context,
    private val hHApiService: HHApiService,
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        Log.d("DEBUG", "Вызов doRequest в RetrofitNetworkClient")
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }

        return withContext(Dispatchers.IO) {
            val userAgent = USER_AGENT
            val token = "Bearer $TOKEN"

            try {
                val response = when (dto) {
                    is VacanciesSearchRequest -> {
                        Log.d("DEBUG", "Вызываю hHApiService.search в RetrofitNetworkClient")
                        hHApiService.search(
                            userAgent = userAgent, body = dto, //временно убрала token = token для тестов без токена
                        )
                    }

                    is VacancyByIdRequest -> hHApiService.getVacancyById(
                        userAgent = userAgent,
                        token = token,
                        vacancyId = dto.id,
                    )

                    else -> Response().apply { resultCode = HTTP_BAD_REQUEST }
                }

                response.apply { resultCode = HTTP_SUCCESS }

            } catch (e: Throwable) {
                Response().apply { resultCode = 500 }
            }
        }
    }


    private fun isConnected(): Boolean {
        return NetworkMonitor.isNetworkAvailable(context)
    }

    companion object {
        private const val HTTP_BAD_REQUEST = 400
        private const val HTTP_SUCCESS = 200
        const val TOKEN = BuildConfig.HH_ACCESS_TOKEN
        const val USER_AGENT = "HamsterHunter/1.0 (s.rubinets@gmail.com)"
    }
}
