package ru.practicum.android.diploma.filter.data.impl

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.filter.data.network.model.AllRegionsRequest
import ru.practicum.android.diploma.filter.data.network.model.RegionsRequest
import ru.practicum.android.diploma.filter.data.network.model.RegionsResponse
import ru.practicum.android.diploma.filter.domain.api.RegionsRepository
import ru.practicum.android.diploma.filter.domain.models.Region
import ru.practicum.android.diploma.util.Constants
import ru.practicum.android.diploma.util.NetworkMonitor
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.util.toRegion
import java.io.IOException

class RegionsRepositoryImpl(
    private val networkClient: NetworkClient,
    private val context: Context
) : RegionsRepository {

    override suspend fun getRegions(countryId: String): Flow<Resource<List<Region>>> =
        fetchRegions(RegionsRequest(countryId))

    override suspend fun getAllRegions(): Flow<Resource<List<Region>>> =
        fetchRegions(AllRegionsRequest())

    private fun fetchRegions(request: Any): Flow<Resource<List<Region>>> = flow {
        try {
            if (!isConnected()) {
                emit(Resource(data = emptyList(), code = -1))
            } else {
                val response = networkClient.doRequest(request)
                if (response is RegionsResponse) {
                    emit(handleRegionsResponse(response))
                } else {
                    emit(Resource(data = emptyList(), code = Constants.HTTP_BAD_REQUEST))
                }
            }
        } catch (e: Exception) {
            emit(handleError(e))
        }
    }

    private fun handleRegionsResponse(response: RegionsResponse): Resource<List<Region>> {
        return when (response.resultCode) {
            Constants.HTTP_SUCCESS -> {
                val regions = response.regionsList.map { it.toRegion() }
                Resource(data = regions, code = Constants.HTTP_SUCCESS)
            }

            Constants.HTTP_NOT_FOUND -> Resource(data = emptyList(), code = Constants.HTTP_NOT_FOUND)
            Constants.HTTP_BAD_REQUEST, Constants.HTTP_SERVER_ERROR ->
                Resource(data = emptyList(), code = Constants.HTTP_SERVER_ERROR)

            -1 -> Resource(data = emptyList(), code = -1)
            else -> Resource(data = emptyList(), code = response.resultCode)
        }
    }

    private fun handleError(e: Exception): Resource<List<Region>> {
        return when (e) {
            is IOException -> {
                Log.e(TAG, "Ошибка сети: ${e.localizedMessage}", e)
                Resource(data = emptyList(), code = -1)
            }

            else -> {
                Log.e(TAG, "Неизвестная ошибка: ${e.localizedMessage}", e)
                Resource(data = emptyList(), code = Constants.HTTP_SERVER_ERROR)
            }
        }
    }


    private fun isConnected(): Boolean {
        return NetworkMonitor.isNetworkAvailable(context)
    }

    companion object {
        private const val TAG = "RegionsRepositoryImpl"
    }
}
