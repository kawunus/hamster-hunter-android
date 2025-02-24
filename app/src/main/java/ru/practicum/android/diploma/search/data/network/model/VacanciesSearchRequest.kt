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
        fun toQueryMap(request: VacanciesSearchRequest, titleSearchField: String? = null): Map<String, String> {
            return mutableMapOf<String, String>().apply {
                put("text", request.text)
                put("page", request.page.toString())
                request.area?.let { put("area", it) }
                request.professionalRole?.let { put("professional_role", it) }
                request.onlyWithSalary?.let { put("only_with_salary", it.toString()) }

                if (request.onlyInTitles == true) {
                    titleSearchField?.let { put("search_field", it) }
                }
            }
        }
    }
}
