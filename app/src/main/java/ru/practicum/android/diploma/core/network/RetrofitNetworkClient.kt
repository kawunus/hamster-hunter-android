package ru.practicum.android.diploma.core.network

import ru.practicum.android.diploma.core.network.dto.Response

class RetrofitNetworkClient(private val hhApiService: HHApiService) : NetworkClient {

    companion object {
        private const val HTTP_BAD_REQUEST = 400
    }

    override suspend fun doRequest(dto: Any): Response {
        return Response().apply { resultCode = HTTP_BAD_REQUEST }
    }
}

