package ru.practicum.android.diploma.core.network


interface NetworkClient {
    suspend fun doRequest(dto:Any) : Response
}
