package ru.practicum.android.diploma.core.data.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

import ru.practicum.android.diploma.search.data.dto.VacanciesSearchRequest
import ru.practicum.android.diploma.search.data.dto.VacanciesSearchResponse
import ru.practicum.android.diploma.vacancy.data.dto.VacancyByIdResponse

interface HHApiService {
    @GET("vacancies")
    suspend fun search(
        @Header("User-Agent") userAgent: String, // Header parameter для авторизации
        @Header("Authorization") token: String,  // access_token для авторизации
        @Body body: VacanciesSearchRequest
    ): VacanciesSearchResponse

    @GET("vacancies/{vacancy_id}")
    fun getVacancyById(
        @Header("User-Agent") userAgent: String, // Header parameter для авторизации
        @Header("Authorization") token: String,  // access_token для авторизации
        @Path("vacancy_id") vacancyId: String, // Path parameter
    ): VacancyByIdResponse
}
