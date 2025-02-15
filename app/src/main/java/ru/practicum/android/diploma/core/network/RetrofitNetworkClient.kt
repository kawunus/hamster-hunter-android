package ru.practicum.android.diploma.core.network

import ru.practicum.android.diploma.core.network.dto.Response

class RetrofitNetworkClient(private val hhApiService: HHApiService) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        return Response().apply { resultCode = 400 }
    }
}
