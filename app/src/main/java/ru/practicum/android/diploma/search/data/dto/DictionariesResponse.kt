package ru.practicum.android.diploma.search.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.core.data.dto.Response

data class DictionariesResponse(
    @SerializedName("vacancy_search_fields")
    val vacancySearchFields: List<VacancySearchField>,
) : Response()
