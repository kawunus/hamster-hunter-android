package ru.practicum.android.diploma.favorites.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.favorites.data.dao.FavoriteVacancyDao
import ru.practicum.android.diploma.favorites.data.entity.FavoriteVacancyEntity
import ru.practicum.android.diploma.favorites.domain.api.FavoriteVacancyRepository
import ru.practicum.android.diploma.favorites.util.FavoriteVacancyConverter
import ru.practicum.android.diploma.search.domain.model.Vacancy

class FavoriteVacancyRepositoryImpl(
    private val favoriteVacancyDao: FavoriteVacancyDao,
    private val converter: FavoriteVacancyConverter
) : FavoriteVacancyRepository {

    override fun getFavoriteVacancies(): Flow<List<Vacancy>> = flow {
        val vacancies = favoriteVacancyDao.getFavoriteVacancies()
        emit(converter.convertListFromVacancyEntity(vacancies))
    }

    override suspend fun addVacancyToFavorites(vacancy: Vacancy) {
        favoriteVacancyDao.addVacancyToFavorites(convertVacancyToVacancyEntity(vacancy))
    }

    override suspend fun deleteVacancyFromFavorites(vacancy: Vacancy) {
        favoriteVacancyDao.deleteVacancyFromFavorites(convertVacancyToVacancyEntity(vacancy))
    }

    override suspend fun isVacancyInFavorites(vacancyId: String): Boolean {
        return favoriteVacancyDao.isVacancyInFavorites(vacancyId)
    }

    private fun convertVacancyToVacancyEntity(vacancy: Vacancy): FavoriteVacancyEntity {
        return converter.map(vacancy, System.currentTimeMillis())
    }
}
