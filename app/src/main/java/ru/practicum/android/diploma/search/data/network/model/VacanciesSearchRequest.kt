package ru.practicum.android.diploma.search.data.network.model

import com.google.gson.annotations.SerializedName

data class VacanciesSearchRequest(
    val text: String,
    val page: Int,
    val area: String?,
    @SerializedName("professional_role")
    val professionalRole: String?,
    @SerializedName("only_with_salary")
    val onlyWithSalary: Boolean?,
)
