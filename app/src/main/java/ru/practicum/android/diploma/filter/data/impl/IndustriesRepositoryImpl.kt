package ru.practicum.android.diploma.filter.data.impl

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import ru.practicum.android.diploma.core.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.filter.data.network.model.IndustriesRequest
import ru.practicum.android.diploma.filter.data.network.model.IndustriesResponse
import ru.practicum.android.diploma.filter.domain.api.IndustriesRepository
import ru.practicum.android.diploma.filter.domain.model.Industry
import ru.practicum.android.diploma.util.Constants
import ru.practicum.android.diploma.util.NetworkMonitor
import ru.practicum.android.diploma.util.Resource
import java.io.IOException

class IndustriesRepositoryImpl(
    private val networkClient: RetrofitNetworkClient, private val context: Context
) : IndustriesRepository {

    override suspend fun getAllIndustries(): Flow<Resource<List<Industry>>> = flow {
        if (!isConnected()) {
            emit(Resource(data = null, code = -1))
        } else {
            try {
                val industriesResponse = networkClient.doRequest(IndustriesRequest) as IndustriesResponse

                val industriesList: List<Industry> = industriesResponse.industriesList.flatMap { category ->
                    category.industries.map { industryDto ->
                        Industry(id = industryDto.id, name = industryDto.name)
                    }
                }.sortedBy { it.name }

                emit(Resource(data = industriesList, code = Constants.HTTP_SUCCESS))
            } catch (e: IOException) {
                Log.e("IndustriesRepositoryImpl", "Ошибка сети: ${e.localizedMessage}", e)
                emit(Resource(data = null, code = -1))
            } catch (e: HttpException) {
                val code = e.code()
                Log.e("IndustriesRepositoryImpl", "HTTP ошибка: Код $code", e)
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
