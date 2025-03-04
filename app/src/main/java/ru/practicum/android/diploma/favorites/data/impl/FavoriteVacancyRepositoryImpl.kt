package ru.practicum.android.diploma.favorites.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.favorites.data.dao.FavoriteVacancyDao
import ru.practicum.android.diploma.favorites.data.entity.FavoriteVacancyEntity
import ru.practicum.android.diploma.favorites.domain.api.FavoriteVacancyRepository
import ru.practicum.android.diploma.favorites.domain.model.FavoritesVacancy
import ru.practicum.android.diploma.favorites.mapper.toDomain
import ru.practicum.android.diploma.favorites.mapper.toEntity

class FavoriteVacancyRepositoryImpl(
    private val favoriteVacancyDao: FavoriteVacancyDao,
) : FavoriteVacancyRepository {

    override fun getFavoriteVacancies(): Flow<List<FavoritesVacancy>> = flow {
        val vacancies = favoriteVacancyDao.getFavoriteVacancies()
        emit(convertListFromVacancyEntity(vacancies))
    }

    override suspend fun addVacancyToFavorites(vacancy: FavoritesVacancy) {
        favoriteVacancyDao.addVacancyToFavorites(vacancy.toEntity(System.currentTimeMillis()))
    }

    override suspend fun deleteVacancyFromFavorites(vacancyId: String) {
        favoriteVacancyDao.deleteVacancyFromFavorites(vacancyId)
    }

    override suspend fun isVacancyInFavorites(vacancyId: String): Boolean {
        return favoriteVacancyDao.isVacancyInFavorites(vacancyId)
    }

    override suspend fun getVacancyById(vacancyId: String): FavoritesVacancy {
        return favoriteVacancyDao.getVacancyById(vacancyId).toDomain()
    }

    private fun convertListFromVacancyEntity(vacancies: List<FavoriteVacancyEntity>): List<FavoritesVacancy> {
        return vacancies.map { vacancy -> vacancy.toDomain() }
    }
}
