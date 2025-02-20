package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.util.FavoriteVacancyConverter

val utilModule = module {

    factory {
        FavoriteVacancyConverter()
    }
}
