package ru.practicum.android.diploma.filter.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.domain.api.IndustriesRepository
import ru.practicum.android.diploma.filter.domain.model.Industry
import ru.practicum.android.diploma.filter.domain.usecase.IndustriesInteractor
import ru.practicum.android.diploma.util.Resource

class IndustriesInteractorImpl(private val repository: IndustriesRepository) : IndustriesInteractor {

    override suspend fun getAllIndustries(): Flow<Resource<List<Industry>>> {
        return repository.getAllIndustries()
    }
}
