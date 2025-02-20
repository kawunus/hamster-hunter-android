package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import ru.practicum.android.diploma.search.data.impl.VacanciesSearchRepositoryImpl

import ru.practicum.android.diploma.search.domain.api.VacanciesSearchRepository


val repositoryModule = module {

    factoryOf(::VacanciesSearchRepositoryImpl) { bind<VacanciesSearchRepository>() }
}
