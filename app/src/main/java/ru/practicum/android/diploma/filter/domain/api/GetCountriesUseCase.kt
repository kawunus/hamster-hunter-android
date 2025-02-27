package ru.practicum.android.diploma.filter.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.util.Resource

interface GetCountriesUseCase {
    suspend fun getCountries(): Flow<Resource<List<Country>>>
}
