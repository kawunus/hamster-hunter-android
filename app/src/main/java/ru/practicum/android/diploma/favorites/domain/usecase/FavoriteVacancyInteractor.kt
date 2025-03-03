package ru.practicum.android.diploma.favorites.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.favorites.domain.model.FavoritesVacancy

interface FavoriteVacancyInteractor {

    fun getFavoriteVacancies(): Flow<List<FavoritesVacancy>>

    suspend fun addVacancyToFavorites(vacancy: FavoritesVacancy)

    suspend fun deleteVacancyFromFavorites(vacancyId: String)

    suspend fun isVacancyInFavorites(vacancyId: String): Boolean

    suspend fun getVacancyById(vacancyId: String): FavoritesVacancy
}
