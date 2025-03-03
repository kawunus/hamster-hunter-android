package ru.practicum.android.diploma.filter.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.domain.api.IndustriesRepository
import ru.practicum.android.diploma.filter.domain.model.Industry
import ru.practicum.android.diploma.filter.domain.usecase.GetIndustriesUseCase
import ru.practicum.android.diploma.util.Resource

class GetIndustriesUseCaseImpl(private val repository: IndustriesRepository) : GetIndustriesUseCase {

    override suspend fun getAllIndustries(): Flow<Resource<List<Industry>>> {
        return repository.getAllIndustries()
    }
}
