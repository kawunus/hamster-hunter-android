package ru.practicum.android.diploma.filter.domain.usecase

import ru.practicum.android.diploma.filter.domain.model.FilterParameters

interface FiltersInteractor {

    fun saveFilters(filters: FilterParameters)

    fun readFilters(): FilterParameters

    fun clearFilters()

    fun checkIfAnyFilterApplied(): Boolean
}
