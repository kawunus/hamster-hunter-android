package ru.practicum.android.diploma.search.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.core.network.dto.Response

data class VacanciesSearchResponse(
    val found: Int, // Общее количество найденных элементов
    val items: List<VacancyDto>, // Список найденных вакансийэлементов
    val page: Int, // Текущая страница
    val pages: Int, // Общее количество страниц
) : Response()

data class Employer(
    val name: String?,
    @SerializedName("logo_urls")
    val logoUrls: LogoUrls?,
)

data class Salary(
    val currency: String?,
    val from: Int?,
    val gross: Boolean?,
    val to: Int?
)

data class Area(
    val name: String?,
)


data class LogoUrls(
    @SerializedName("`90`")
    val logoUrl90: String?,
)

