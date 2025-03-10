package ru.practicum.android.diploma.search.data.dto

data class VacanciesSearchRequest(
    val text: String,
    val page: Int,
    val area: String?,
    val industry: String?,
    val salary: Int?,
    val onlyWithSalary: Boolean?,
    val onlyInTitles: Boolean?

) {
    companion object {
        const val TEXT = "text"
        const val PAGE = "page"
        const val AREA = "area"
        const val INDUSTRY = "industry"
        const val SALARY = "salary"
        const val ONLY_WITH_SALARY = "only_with_salary"
        const val SEARCH_FIELD = "search_field"
    }
}
