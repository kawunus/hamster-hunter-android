package ru.practicum.android.diploma.di

import android.content.Context
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.core.data.database.AppDatabase

val dataModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "hamster_hunter_database.db").build()
    }

    single {
        androidContext().getSharedPreferences("hamster_hunter_shared_preferences", Context.MODE_PRIVATE)
    }
}
