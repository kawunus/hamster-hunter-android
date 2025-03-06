package ru.practicum.android.diploma.filter.data.sharedprefs

import android.content.SharedPreferences

class SharedPrefsStorage(private val filtersPrefs: SharedPreferences) {

    fun getFiltersPrefs(): SharedPreferences {
        return filtersPrefs
    }
}
