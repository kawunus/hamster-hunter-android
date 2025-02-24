package ru.practicum.android.diploma.favorites.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.favorites.domain.api.FavoriteVacancyInteractor
import ru.practicum.android.diploma.favorites.domain.api.FavoriteVacancyRepository
import ru.practicum.android.diploma.favorites.domain.model.FavoriteVacancy

class FavoriteVacancyInteractorImpl(private val repository: FavoriteVacancyRepository) : FavoriteVacancyInteractor {

    override fun getFavoriteVacancies(): Flow<List<FavoriteVacancy>> {
        return repository.getFavoriteVacancies()
    }

    override suspend fun addVacancyToFavorites(vacancy: FavoriteVacancy) {
        repository.addVacancyToFavorites(vacancy)
    }

    override suspend fun deleteVacancyFromFavorites(vacancyId: String) {
        repository.deleteVacancyFromFavorites(vacancyId)
    }

    override suspend fun isVacancyInFavorites(vacancyId: String): Boolean {
        return repository.isVacancyInFavorites(vacancyId)
    }
}
