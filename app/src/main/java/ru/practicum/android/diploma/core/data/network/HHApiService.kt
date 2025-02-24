package ru.practicum.android.diploma.core.data.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.search.data.dto.VacanciesSearchResponse
import ru.practicum.android.diploma.vacancy.data.network.model.VacancyByIdResponse

interface HHApiService {
    @GET("vacancies")
    suspend fun search(
        @Header("User-Agent") userAgent: String, // Header parameter для авторизации
        @Query("text") text: String
    ): VacanciesSearchResponse

    @GET("vacancies/{vacancy_id}")
    suspend fun getVacancyById(
        @Header("User-Agent") userAgent: String, // Header parameter для авторизации
        @Path("vacancy_id") vacancyId: String, // Path parameter
    ): VacancyByIdResponse
}
