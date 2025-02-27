package ru.practicum.android.diploma.filter.data.impl

import CountriesResponse
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.filter.data.network.model.CountriesRequest
import ru.practicum.android.diploma.filter.domain.api.CountriesRepository
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.util.Constants
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.util.toCountry
import java.io.IOException

class CountriesRepositoryImpl(
    private val networkClient: NetworkClient
) : CountriesRepository {
    override suspend fun getCountries(): Flow<Resource<List<Country>>> = flow {
        try {
            var response = networkClient.doRequest(CountriesRequest)

            if (response !is CountriesResponse) {
                emit(Resource(data = null, code = Constants.HTTP_BAD_REQUEST))
            }

            when (response.resultCode) {
                Constants.HTTP_SUCCESS -> {
                    response = response as CountriesResponse
                    val countries = response.countriesList.map { it.toCountry() }
                    emit(Resource(data = countries, code = Constants.HTTP_SUCCESS))
                }

                Constants.HTTP_NOT_FOUND -> {
                    emit(Resource(data = null, code = Constants.HTTP_NOT_FOUND))
                }

                Constants.HTTP_BAD_REQUEST, Constants.HTTP_SERVER_ERROR -> {
                    emit(Resource(data = null, code = Constants.HTTP_SERVER_ERROR))
                }

                -1 -> {
                    emit(Resource(data = null, code = -1))
                }

                else -> {
                    emit(Resource(data = null, code = response.resultCode))
                }
            }
        } catch (e: IOException) {
            Log.e("CountriesRepositoryImpl", "Ошибка сети: ${e.localizedMessage}", e)
            emit(Resource(data = null, code = -1))
        }
    }
}
