package ru.practicum.android.diploma.core.data.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.search.data.network.model.DictionariesResponse
import ru.practicum.android.diploma.search.data.network.model.VacanciesSearchResponse
import ru.practicum.android.diploma.vacancy.data.dto.VacancyByIdResponse

interface HHApiService {
    @GET("vacancies")
    suspend fun search(
        @Header("User-Agent") userAgent: String,
        @QueryMap(encoded = true) queryMap: Map<String, String>
    ): VacanciesSearchResponse

    @GET("vacancies/{vacancy_id}")
    suspend fun getVacancyById(
        @Header("User-Agent") userAgent: String,
        @Path("vacancy_id") vacancyId: String,
    ): VacancyByIdResponse

    @GET("dictionaries")
    suspend fun getDictionaries(
        // Для получения справочника с возможными значениями vacancy_search_fields
        @Header("User-Agent") userAgent: String,
    ): DictionariesResponse
}
