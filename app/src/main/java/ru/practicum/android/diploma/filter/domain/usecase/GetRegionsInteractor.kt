package ru.practicum.android.diploma.filter.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.domain.model.Region
import ru.practicum.android.diploma.util.Resource

interface GetRegionsInteractor {
    suspend fun getRegions(countryId: String): Flow<Resource<List<Region>>>
    suspend fun getAllRegions(): Flow<Resource<List<Region>>>
}
