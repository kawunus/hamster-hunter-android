package ru.practicum.android.diploma.filter.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.domain.model.Country
import ru.practicum.android.diploma.util.Resource

interface GetCountriesUseCase {
    suspend fun getCountries(): Flow<Resource<List<Country>>>
}
