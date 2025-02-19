package ru.practicum.android.diploma.search.data.dto

data class VacancyDto(
    val id: String, // Идентификатор вакансии
    val name: String, // Название вакансии
    val salary: Salary, // Зарплата
    val employer: Employer, // Информация о компании-работодателе
    val area: Area // Регион


)
