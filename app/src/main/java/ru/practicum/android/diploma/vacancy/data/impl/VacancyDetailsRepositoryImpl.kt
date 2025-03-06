package ru.practicum.android.diploma.vacancy.data.impl

import android.content.Context
import android.content.Intent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.util.Constants
import ru.practicum.android.diploma.vacancy.data.network.model.VacancyByIdRequest
import ru.practicum.android.diploma.vacancy.data.network.model.VacancyByIdResponse
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsRepository
import ru.practicum.android.diploma.vacancy.domain.model.ErrorType
import ru.practicum.android.diploma.vacancy.domain.model.NetworkResult
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetails
import ru.practicum.android.diploma.vacancy.mapper.Mapper.toVacancyDetails
import java.io.IOException

class VacancyDetailsRepositoryImpl(
    private val context: Context,
    private val networkClient: NetworkClient,
) : VacancyDetailsRepository {
    override fun openUrlShare(shareUrl: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = TYPE_TEXT_PLAIN
            putExtra(Intent.EXTRA_TEXT, shareUrl)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(shareIntent)
    }

    override suspend fun findVacancyDetails(vacancyId: String): Flow<NetworkResult<VacancyDetails?, ErrorType>> = flow {
        try {
            val response = networkClient.doRequest(VacancyByIdRequest(vacancyId))
            if (response.resultCode == Constants.HTTP_NOT_FOUND) {
                emit(NetworkResult.Error(ErrorType.NOT_FOUND))
            } else {
                emit(NetworkResult.Success((response as VacancyByIdResponse).toVacancyDetails()))
            }
        } catch (e: IOException) {
            emit(NetworkResult.Error(ErrorType.NO_INTERNET))
        } catch (e: HttpException) {
            emit(NetworkResult.Error(ErrorType.UNKNOWN))
        }
    }

    private companion object {
        const val TYPE_TEXT_PLAIN = "text/plain"
    }
}
