package ru.practicum.android.diploma.search.data.dto

import ru.practicum.android.diploma.core.data.dto.Response

data class VacanciesSearchResponse(
    val found: Int, // Общее количество найденных элементов
    val items: List<VacancyDto>, // Список найденных вакансийэлементов
    val page: Int, // Текущая страница
    val pages: Int, // Общее количество страниц
) : Response()
