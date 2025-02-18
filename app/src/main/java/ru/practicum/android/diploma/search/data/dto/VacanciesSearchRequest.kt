package ru.practicum.android.diploma.search.data.dto

import com.google.gson.annotations.SerializedName

data class VacanciesSearchRequest(
    val page: Int,
    val text: String,
    val area: String?,
    @SerializedName("professional_role")
    val professionalRole: String?,
    @SerializedName("only_with_salary")
    val onlyWithSalary: Boolean?,
    )
