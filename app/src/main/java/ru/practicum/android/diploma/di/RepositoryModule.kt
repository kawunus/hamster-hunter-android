package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.data.impl.FavoriteVacancyRepositoryImpl
import ru.practicum.android.diploma.favorites.domain.api.FavoriteVacancyRepository
import ru.practicum.android.diploma.search.data.impl.VacanciesSearchRepositoryImpl
import ru.practicum.android.diploma.search.domain.api.VacanciesSearchRepository

val repositoryModule = module {
    factoryOf(::VacanciesSearchRepositoryImpl) { bind<VacanciesSearchRepository>() }

    singleOf(::FavoriteVacancyRepositoryImpl) {bind<FavoriteVacancyRepository>()}
}
