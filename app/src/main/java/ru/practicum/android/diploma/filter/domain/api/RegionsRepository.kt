package ru.practicum.android.diploma.filter.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.domain.model.Region
import ru.practicum.android.diploma.util.Resource

interface RegionsRepository {
    suspend fun getRegions(countryId: String): Flow<Resource<List<Region>>>
    suspend fun getAllRegions(): Flow<Resource<List<Region>>>
}
