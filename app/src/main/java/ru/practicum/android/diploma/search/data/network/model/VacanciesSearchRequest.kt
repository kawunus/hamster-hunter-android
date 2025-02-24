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
    val onlyInTitles: Boolean?,
) {
    fun toQueryMap(titleSearchField: String? = null): Map<String, String> {
        return mutableMapOf<String, String>().apply {
            put("text", text)
            put("page", page.toString())
            area?.let { put("area", it) }
            professionalRole?.let { put("professional_role", it) }
            onlyWithSalary?.let { put("only_with_salary", it.toString()) }

            // Если onlyInTitles == true, передаем titleSearchField (может быть null, если оно не задано)
            if (onlyInTitles == true) {
                titleSearchField?.let { put("search_field", it) }
            }
        }
    }
}
