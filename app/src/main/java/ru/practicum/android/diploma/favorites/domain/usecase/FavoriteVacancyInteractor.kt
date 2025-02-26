package ru.practicum.android.diploma.favorites.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.model.Vacancy

interface FavoriteVacancyInteractor {

    fun getFavoriteVacancies(): Flow<List<Vacancy>>

    suspend fun addVacancyToFavorites(vacancy: Vacancy)

    suspend fun deleteVacancyFromFavorites(vacancyId: String)

    suspend fun isVacancyInFavorites(vacancyId: String): Boolean
}
