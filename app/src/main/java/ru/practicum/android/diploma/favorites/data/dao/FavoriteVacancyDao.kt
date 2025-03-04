package ru.practicum.android.diploma.favorites.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.favorites.data.entity.FavoriteVacancyEntity

@Dao
interface FavoriteVacancyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addVacancyToFavorites(vacancy: FavoriteVacancyEntity)

    @Query("DELETE FROM favorite_vacancies WHERE id = :vacancyId")
    suspend fun deleteVacancyFromFavorites(vacancyId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_vacancies WHERE id = :vacancyId)")
    suspend fun isVacancyInFavorites(vacancyId: String): Boolean

    @Query("SELECT * FROM favorite_vacancies ORDER BY added_at DESC")
    suspend fun getFavoriteVacancies(): List<FavoriteVacancyEntity>

    @Query("SELECT * FROM favorite_vacancies WHERE id = :vacancyId")
    suspend fun getVacancyById(vacancyId: String): FavoriteVacancyEntity

    @Query("UPDATE favorite_vacancies SET added_at = :newAddedAt WHERE id = :vacancyId")
    suspend fun updateAddedAt(vacancyId: String, newAddedAt: Long)
}
