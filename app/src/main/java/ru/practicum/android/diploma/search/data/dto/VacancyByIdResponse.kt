package ru.practicum.android.diploma.search.data.dto

import ru.practicum.android.diploma.core.network.dto.Response

data class VacancyByIdResponse(
    val id: String, // Идентификатор вакансии
    val name: String, // Название вакансии
    val salary: Salary, // Зарплата
    val employer: Employer, // Информация о компании-работодателе
    //TODO ...

) : Response()

