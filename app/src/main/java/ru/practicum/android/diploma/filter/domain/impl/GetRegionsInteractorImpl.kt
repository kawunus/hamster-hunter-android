package ru.practicum.android.diploma.filter.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.domain.api.GetRegionsInteractor
import ru.practicum.android.diploma.filter.domain.api.RegionsRepository
import ru.practicum.android.diploma.filter.domain.models.Region
import ru.practicum.android.diploma.util.Resource

class GetRegionsInteractorImpl(
    private val regionsRepository: RegionsRepository
) : GetRegionsInteractor {
    override suspend fun getRegions(countryId: String): Flow<Resource<List<Region>>> {
        return regionsRepository.getRegions(countryId)
    }
}
