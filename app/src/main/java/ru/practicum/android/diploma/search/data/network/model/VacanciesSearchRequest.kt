package ru.practicum.android.diploma.search.data.network.model

data class VacanciesSearchRequest(
    val text: String,
    val page: Int,
    val area: String?,
    val professionalRole: String?,
    val salary: Int?,
    val onlyWithSalary: Boolean?,
    val onlyInTitles: Boolean?

) {
    companion object {
        const val TEXT = "text"
        const val PAGE = "page"
        const val AREA = "area"
        const val PROFESSIONAL_ROLE = "professional_role"
        const val SALARY = "salary"
        const val ONLY_WITH_SALARY = "only_with_salary"
        const val SEARCH_FIELD = "search_field"
    }
}
