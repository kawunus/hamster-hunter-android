package ru.practicum.android.diploma.filter.domain.impl

import ru.practicum.android.diploma.filter.domain.api.FiltersRepository
import ru.practicum.android.diploma.filter.domain.model.FilterParameters
import ru.practicum.android.diploma.filter.domain.usecase.FiltersInteractor

class FiltersInteractorImpl(private val repository: FiltersRepository) : FiltersInteractor {
    override fun saveFilters(filters: FilterParameters) {
        repository.saveFilters(filters)
    }

    override fun readFilters(): FilterParameters {
        return repository.readFilters()
    }

    override fun clearFilters() {
        repository.clearFilters()
    }

    override fun checkIfAnyFilterApplied(): Boolean {
        return repository.checkIfAnyFilterApplied()
    }
}
