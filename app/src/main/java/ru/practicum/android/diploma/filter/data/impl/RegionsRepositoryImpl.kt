package ru.practicum.android.diploma.filter.data.impl

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.filter.data.network.model.RegionsRequest
import ru.practicum.android.diploma.filter.data.network.model.RegionsResponse
import ru.practicum.android.diploma.filter.domain.api.RegionsRepository
import ru.practicum.android.diploma.filter.domain.models.Region
import ru.practicum.android.diploma.util.Constants
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.util.toRegion
import java.io.IOException

class RegionsRepositoryImpl(
    private val networkClient: NetworkClient
) : RegionsRepository {
    override suspend fun getRegions(countryId: String): Flow<Resource<List<Region>>> = flow {
        try {
            var response = networkClient.doRequest(RegionsRequest(countryId))
            Log.e("RegionSearch", "Repo код: ${response.resultCode}")
            if (response !is RegionsResponse) {
                emit(Resource(data = null, code = Constants.HTTP_BAD_REQUEST))
            } else {
                when (response.resultCode) {
                    Constants.HTTP_SUCCESS -> {
                        response = response as RegionsResponse
                        val regions = response.regionsList.map { it.toRegion() }
                        emit(Resource(data = regions, code = Constants.HTTP_SUCCESS))
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
            }
        } catch (e: IOException) {
            Log.e("RegionsRepositoryImpl", "Ошибка сети: ${e.localizedMessage}", e)
            emit(Resource(data = null, code = -1))
        }
    }
}
