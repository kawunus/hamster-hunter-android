package ru.practicum.android.diploma.filter.data.impl

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import ru.practicum.android.diploma.core.data.network.HHApiService
import ru.practicum.android.diploma.filter.domain.api.CountriesRepository
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.util.Constants
import ru.practicum.android.diploma.util.NetworkMonitor
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.util.toCountry
import java.io.IOException

class CountriesRepositoryImpl(
    private val networkClient: HHApiService,
    private val context: Context
) : CountriesRepository {

    override suspend fun getCountries(): Flow<Resource<List<Country>>> = flow {
        if (!isConnected()) {
            emit(Resource(data = null, code = -1))
        } else {
            try {
                val listDto = networkClient.getCountries()
                val countries = listDto.map { it.toCountry() }
                val sortedCountries = countries.sortedBy { it.name == "Другие регионы" }
                emit(Resource(data = sortedCountries, code = Constants.HTTP_SUCCESS))

            } catch (e: IOException) {
                Log.e("CountriesRepositoryImpl", "Ошибка сети: ${e.localizedMessage}", e)
                emit(Resource(data = null, code = -1))
            } catch (e: HttpException) {
                val code = e.code()
                Log.e("CountriesRepositoryImpl", "HTTP ошибка: Код $code", e)
                emit(
                    Resource(
                        data = null,
                        code = Constants.HTTP_SERVER_ERROR
                    )
                )
            }
        }
    }

    private fun isConnected(): Boolean {
        return NetworkMonitor.isNetworkAvailable(context)
    }
}
