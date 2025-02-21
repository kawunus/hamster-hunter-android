package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.domain.api.FavoriteVacancyInteractor
import ru.practicum.android.diploma.favorites.domain.impl.FavoriteVacancyInteractorImpl
import ru.practicum.android.diploma.search.domain.api.VacanciesSearchInteractor
import ru.practicum.android.diploma.search.domain.impl.VacanciesSearchInteractorImpl

val interactorModule = module {
    factoryOf(::VacanciesSearchInteractorImpl) { bind<VacanciesSearchInteractor>() }

    singleOf(::FavoriteVacancyInteractorImpl) { bind<FavoriteVacancyInteractor>() }
}
