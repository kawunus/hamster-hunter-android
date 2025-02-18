package ru.practicum.android.diploma.search.data.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import ru.practicum.android.diploma.search.data.dto.VacanciesSearchRequest
import ru.practicum.android.diploma.search.data.dto.VacanciesSearchResponse

interface HHApiService {
    @GET("vacancies")
    suspend fun search(
        @Header("Authorization") token: String,
        @Body body: VacanciesSearchRequest
    ): VacanciesSearchResponse
}
