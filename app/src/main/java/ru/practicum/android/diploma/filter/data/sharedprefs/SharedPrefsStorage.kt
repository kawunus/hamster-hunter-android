package ru.practicum.android.diploma.filter.data.sharedprefs

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class SharedPrefsStorage(context: Context) {

    private val filtersPrefs =
        context.getSharedPreferences(
            HH_SHARED_PREFS_NAME,
            MODE_PRIVATE
        )

    fun getFiltersPrefs(): SharedPreferences {
        return filtersPrefs
    }

    private companion object {
        const val HH_SHARED_PREFS_NAME = "hamster_hunter_shared_preferences"
    }
}
