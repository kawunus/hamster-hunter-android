package ru.practicum.android.diploma.filter.data.impl

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.filter.data.sharedprefs.SharedPrefsStorage
import ru.practicum.android.diploma.filter.domain.api.FiltersRepository
import ru.practicum.android.diploma.filter.domain.model.FilterParameters

class FiltersRepositoryImpl(sharedPrefsStorage: SharedPrefsStorage, private val gson: Gson) :
    FiltersRepository {
    private val sharedPreferences = sharedPrefsStorage.getFiltersPrefs()

    override fun saveFilters(filters: FilterParameters) {
        sharedPreferences.edit()
            .putString(SHARED_PREFS_FILTERS_KEY, gson.toJson(filters))
            .apply()
    }

    override fun readFilters(): FilterParameters {
        val filtersJson = sharedPreferences.getString(SHARED_PREFS_FILTERS_KEY, null)
        return when {
            filtersJson != null -> gson.fromJson(filtersJson, filterParametersType)
            else -> FilterParameters()
        }
    }

    override fun checkIfAnyFilterApplied(): Boolean {
        val filters = readFilters()
        with(filters) {
            val parametersList = listOf(area, professionalRole, salary, onlyWithSalary, onlyInTitles)
            return parametersList.any { it != null }
        }
    }

    override fun clearFilters() {
        sharedPreferences.edit().remove(SHARED_PREFS_FILTERS_KEY).apply()
    }

    private companion object {
        private val filterParametersType = object : TypeToken<FilterParameters>() {}.type
        const val SHARED_PREFS_FILTERS_KEY = "key_for_saved_filters"
    }
}
