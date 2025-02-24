package ru.practicum.android.diploma.search.data.network.model

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.core.data.network.dto.Response
import ru.practicum.android.diploma.search.data.dto.VacancySearchField

data class DictionariesResponse(
    @SerializedName("vacancy_search_fields")
    val vacancySearchFields: List<VacancySearchField>,
) : Response()
