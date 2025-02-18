package ru.practicum.android.diploma.core.network

import ru.practicum.android.diploma.core.network.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}
