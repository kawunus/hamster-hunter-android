package ru.practicum.android.diploma.vacancy.data.impl

import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.vacancy.data.network.model.VacancyByIdRequest
import ru.practicum.android.diploma.vacancy.data.network.model.VacancyByIdResponse
import ru.practicum.android.diploma.vacancy.domain.api.GetVacancyRepository
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetails
import ru.practicum.android.diploma.vacancy.mapper.Mapper.toVacancyDetails

class GetVacancyRepositoryImpl(
    private val networkClient: NetworkClient
) : GetVacancyRepository {
    override suspend fun execute(vacancyId: Int): VacancyDetails? {
        val response = networkClient.doRequest(VacancyByIdRequest(vacancyId.toString()))
        return when (response.resultCode) {
            200 -> (response as VacancyByIdResponse).toVacancyDetails()
            else -> null
        }
    }

}
