package ru.practicum.android.diploma.favorites.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.favorites.domain.model.FavoriteVacancy

interface FavoriteVacancyInteractor {

    fun getFavoriteVacancies(): Flow<List<FavoriteVacancy>>

    suspend fun addVacancyToFavorites(vacancy: FavoriteVacancy)

    suspend fun deleteVacancyFromFavorites(vacancyId: String)

    suspend fun isVacancyInFavorites(vacancyId: String): Boolean
}
