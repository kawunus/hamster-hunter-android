package ru.practicum.android.diploma.search.data.network.model

import ru.practicum.android.diploma.core.data.network.dto.Response
import ru.practicum.android.diploma.search.data.dto.VacancyDto

data class VacanciesSearchResponse(
    val found: Int, // Общее количество найденных элементов
    val items: List<VacancyDto>, // Список найденных вакансийэлементов
    val page: Int, // Текущая страница
    val pages: Int, // Общее количество страниц
) : Response()
