package ru.practicum.android.diploma.filter.domain.api

import ru.practicum.android.diploma.filter.domain.model.FilterParameters

interface FiltersRepository {

    fun save(filters: FilterParameters)

    fun read(): FilterParameters

    fun clear()
}

