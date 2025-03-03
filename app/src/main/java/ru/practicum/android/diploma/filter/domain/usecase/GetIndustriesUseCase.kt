package ru.practicum.android.diploma.filter.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.domain.model.Industry
import ru.practicum.android.diploma.util.Resource

interface GetIndustriesUseCase {

    suspend fun getAllIndustries(): Flow<Resource<List<Industry>>>
}
