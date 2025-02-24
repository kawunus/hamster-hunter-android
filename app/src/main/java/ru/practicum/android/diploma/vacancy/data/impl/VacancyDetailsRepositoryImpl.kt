package ru.practicum.android.diploma.vacancy.data.impl

import android.content.Context
import android.content.Intent
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.vacancy.data.network.model.VacancyByIdRequest
import ru.practicum.android.diploma.vacancy.data.network.model.VacancyByIdResponse
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsRepository
import ru.practicum.android.diploma.vacancy.mapper.Mapper.toVacancyDetails

class VacancyDetailsRepositoryImpl(
    private val context: Context,
    private val networkClient: NetworkClient,
) :
    VacancyDetailsRepository {
    override fun openUrlShare(shareUrl: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareUrl)
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    override suspend fun findVacancyDetails(vacancyId: Int) = flow {
        val response = networkClient.doRequest(VacancyByIdRequest(vacancyId.toString()))
        when (response.resultCode) {
            SUCCESS_CODE -> emit((response as VacancyByIdResponse).toVacancyDetails())
            else -> emit(null)
        }
    }

    companion object {
        const val SUCCESS_CODE = 200
    }
}
