package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.domain.impl.FavoriteVacancyInteractorImpl
import ru.practicum.android.diploma.favorites.domain.usecase.FavoriteVacancyInteractor
import ru.practicum.android.diploma.search.domain.impl.VacanciesSearchInteractorImpl
import ru.practicum.android.diploma.search.domain.usecase.VacanciesSearchInteractor
import ru.practicum.android.diploma.vacancy.domain.impl.VacancyDetailsInteractorImpl
import ru.practicum.android.diploma.vacancy.domain.usecase.VacancyDetailsInteractor

val interactorModule = module {
    singleOf(::VacanciesSearchInteractorImpl) { bind<VacanciesSearchInteractor>() }

    singleOf(::FavoriteVacancyInteractorImpl) { bind<FavoriteVacancyInteractor>() }

    singleOf(::VacancyDetailsInteractorImpl) { bind<VacancyDetailsInteractor>() }
}
