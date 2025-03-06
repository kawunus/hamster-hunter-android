package ru.practicum.android.diploma.filter.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.domain.api.CountriesRepository
import ru.practicum.android.diploma.filter.domain.model.Country
import ru.practicum.android.diploma.filter.domain.usecase.GetCountriesUseCase
import ru.practicum.android.diploma.util.Resource

class GetCountriesUseCaseImpl(
    private val countriesRepository: CountriesRepository
) : GetCountriesUseCase {
    override suspend fun getCountries(): Flow<Resource<List<Country>>> {
        return countriesRepository.getCountries()
    }
}
