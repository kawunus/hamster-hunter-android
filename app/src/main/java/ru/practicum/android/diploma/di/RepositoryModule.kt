package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.data.impl.FavoriteVacancyRepositoryImpl
import ru.practicum.android.diploma.favorites.domain.api.FavoriteVacancyRepository
import ru.practicum.android.diploma.search.data.impl.VacanciesSearchRepositoryImpl
import ru.practicum.android.diploma.search.domain.api.VacanciesSearchRepository
import ru.practicum.android.diploma.vacancy.data.impl.GetVacancyRepositoryImpl
import ru.practicum.android.diploma.vacancy.domain.api.GetVacancyRepository

val repositoryModule = module {
    factoryOf(::VacanciesSearchRepositoryImpl) { bind<VacanciesSearchRepository>() }
    factoryOf(::GetVacancyRepositoryImpl) { bind<GetVacancyRepository>() }
    singleOf(::FavoriteVacancyRepositoryImpl) { bind<FavoriteVacancyRepository>() }
}
