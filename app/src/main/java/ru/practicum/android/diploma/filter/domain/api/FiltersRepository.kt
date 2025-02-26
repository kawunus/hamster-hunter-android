package ru.practicum.android.diploma.filter.domain.api

import ru.practicum.android.diploma.filter.domain.model.FilterParameters

interface FiltersRepository {

    fun saveFilters(filters: FilterParameters)

    fun readFilters(): FilterParameters

    fun clearFilters()
}

