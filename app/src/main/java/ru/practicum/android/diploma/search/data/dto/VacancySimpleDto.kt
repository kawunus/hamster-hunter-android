package ru.practicum.android.diploma.search.data.dto

import com.google.gson.annotations.SerializedName

data class VacancySimpleDto(
    val id: String, // Идентификатор вакансии
    val name: String, // Название вакансии
    val salary: Salary, // Зарплата
    val employer: Employer, // Информация о компании-работодателе


    @SerializedName("published_at")
    val publishedAt: String, // Дата и время публикации вакансии - оставила, вдруг захотим делать сортировку
)
