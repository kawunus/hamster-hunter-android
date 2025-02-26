package ru.practicum.android.diploma.search.data.network.model

import com.google.gson.annotations.SerializedName

data class VacanciesSearchRequest(
    val text: String,
    val page: Int,
    val area: String?,
    @SerializedName("professional_role") val professionalRole: String?,
    @SerializedName("only_with_salary") val onlyWithSalary: Boolean?,
    val onlyInTitles: Boolean?

) {
    companion object {
        const val TEXT = "text"
        const val PAGE = "page"
        const val AREA = "area"
        const val PROFESSIONAL_ROLE = "professional_role"
        const val ONLY_WITH_SALARY = "only_with_salary"
        const val SEARCH_FIELD = "search_field"
    }
}
