package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.data.impl.FavoriteVacancyRepositoryImpl
import ru.practicum.android.diploma.favorites.domain.api.FavoriteVacancyRepository
import ru.practicum.android.diploma.filter.data.impl.FiltersRepositoryImpl
import ru.practicum.android.diploma.filter.domain.api.FiltersRepository
import ru.practicum.android.diploma.search.data.impl.VacanciesSearchRepositoryImpl
import ru.practicum.android.diploma.search.domain.api.VacanciesSearchRepository
import ru.practicum.android.diploma.vacancy.data.impl.VacancyDetailsRepositoryImpl
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsRepository

val repositoryModule = module {
    singleOf(::VacanciesSearchRepositoryImpl) { bind<VacanciesSearchRepository>() }

    singleOf(::FavoriteVacancyRepositoryImpl) { bind<FavoriteVacancyRepository>() }

    singleOf(::VacancyDetailsRepositoryImpl) { bind<VacancyDetailsRepository>() }

    singleOf(::FiltersRepositoryImpl) { bind<FiltersRepository>() }

}
