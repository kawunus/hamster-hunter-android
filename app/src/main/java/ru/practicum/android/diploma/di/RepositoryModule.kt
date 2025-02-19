package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.search.data.impl.VacanciesSearchRepositoryImpl

import ru.practicum.android.diploma.search.domain.api.VacanciesSearchRepository


val repositoryModule = module {
    factory<VacanciesSearchRepository> {
        VacanciesSearchRepositoryImpl()
    }
}
