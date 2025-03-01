package ru.practicum.android.diploma.filter.data.impl

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import ru.practicum.android.diploma.core.data.network.HHApiService
import ru.practicum.android.diploma.filter.domain.api.CountriesRepository
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.util.Constants
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.util.toCountry
import java.io.IOException

class CountriesRepositoryImpl(
    private val networkClient: HHApiService
) : CountriesRepository {

    override suspend fun getCountries(): Flow<Resource<List<Country>>> = flow {
        try {
            val listDto = networkClient.getCountries()
            val countries = listDto.map { it.toCountry() }

            val sortedCountries = countries.sortedBy { it.name == "Другие регионы" }

            emit(Resource(data = sortedCountries, code = Constants.HTTP_SUCCESS))

        } catch (e: HttpException) {
            val code = e.code()
            emit(Resource(data = null, code = code))
        } catch (e: IOException) {
            Log.e("CountriesRepositoryImpl", "Ошибка сети: ${e.localizedMessage}", e)
            emit(Resource(data = null, code = -1))
        } catch (e: IOException) {
            Log.e("CountriesRepositoryImpl", "Другая ошибка: ${e.localizedMessage}", e)
            emit(Resource(data = null, code = Constants.HTTP_SERVER_ERROR))
        }
    }
}

