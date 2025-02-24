package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.domain.api.FavoriteVacancyInteractor
import ru.practicum.android.diploma.favorites.domain.impl.FavoriteVacancyInteractorImpl
import ru.practicum.android.diploma.search.domain.api.VacanciesSearchInteractor
import ru.practicum.android.diploma.search.domain.impl.VacanciesSearchInteractorImpl
import ru.practicum.android.diploma.vacancy.domain.impl.GetVacancyUseCaseImpl
import ru.practicum.android.diploma.vacancy.domain.usecase.GetVacancyUseCase
import ru.practicum.android.diploma.sharing.domain.api.SharingInteractor
import ru.practicum.android.diploma.sharing.domain.impl.SharingInteractorImpl

val interactorModule = module {
    factoryOf(::VacanciesSearchInteractorImpl) { bind<VacanciesSearchInteractor>() }

    singleOf(::FavoriteVacancyInteractorImpl) { bind<FavoriteVacancyInteractor>() }

    singleOf(::SharingInteractorImpl) { bind<SharingInteractor>() }
}
