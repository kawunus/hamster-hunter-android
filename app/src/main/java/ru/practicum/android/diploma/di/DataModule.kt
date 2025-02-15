package ru.practicum.android.diploma.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.core.database.AppDatabase

val dataModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "hamster_hunter_db").build()
    }
}
