package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import ru.practicum.android.diploma.search.domain.api.VacanciesSearchInteractor
import ru.practicum.android.diploma.search.domain.impl.VacanciesSearchInteractorImpl

val interactorModule = module {
    factoryOf(::VacanciesSearchInteractorImpl) { bind<VacanciesSearchInteractor>() }
}
