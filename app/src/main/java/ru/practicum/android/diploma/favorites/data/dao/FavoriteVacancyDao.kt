package ru.practicum.android.diploma.favorites.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.favorites.data.entity.FavoriteVacancyEntity
import ru.practicum.android.diploma.search.domain.model.Vacancy

@Dao
interface FavoriteVacancyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addVacancyToFavorites(vacancy: FavoriteVacancyEntity)

    @Delete
    suspend fun deleteVacancyFromFavorites(vacancy: FavoriteVacancyEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_vacancies WHERE id = :vacancyId)")
    suspend fun isVacancyInFavorites(vacancyId: String): Boolean

    @Query("SELECT * FROM favorite_vacancies ORDER BY added_at")
    suspend fun getFavoriteVacancies(): List<FavoriteVacancyEntity>
}
