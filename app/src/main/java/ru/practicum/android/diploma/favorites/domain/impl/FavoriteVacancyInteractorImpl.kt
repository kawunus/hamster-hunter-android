package ru.practicum.android.diploma.favorites.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.favorites.domain.api.FavoriteVacancyRepository
import ru.practicum.android.diploma.favorites.domain.model.FavoritesVacancy
import ru.practicum.android.diploma.favorites.domain.usecase.FavoriteVacancyInteractor

class FavoriteVacancyInteractorImpl(private val repository: FavoriteVacancyRepository) :
    FavoriteVacancyInteractor {

    override fun getFavoriteVacancies(): Flow<List<FavoritesVacancy>> {
        return repository.getFavoriteVacancies()
    }

    override suspend fun addVacancyToFavorites(vacancy: FavoritesVacancy) {
        repository.addVacancyToFavorites(vacancy)
    }

    override suspend fun deleteVacancyFromFavorites(vacancyId: String) {
        repository.deleteVacancyFromFavorites(vacancyId)
    }

    override suspend fun isVacancyInFavorites(vacancyId: String): Boolean {
        return repository.isVacancyInFavorites(vacancyId)
    }

    override suspend fun getVacancyById(vacancyId: String): FavoritesVacancy {
        return repository.getVacancyById(vacancyId)
    }
}
