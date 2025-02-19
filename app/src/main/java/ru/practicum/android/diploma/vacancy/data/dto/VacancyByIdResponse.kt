package ru.practicum.android.diploma.vacancy.data.dto

import ru.practicum.android.diploma.core.data.network.dto.Response
import ru.practicum.android.diploma.search.data.dto.Employer
import ru.practicum.android.diploma.search.data.dto.Salary

data class VacancyByIdResponse(
    val id: String, // Идентификатор вакансии
    val name: String, // Название вакансии
    val salary: Salary, // Зарплата
    val employer: Employer, // Информация о компании-работодателе
    //TODO ...

) : Response()

