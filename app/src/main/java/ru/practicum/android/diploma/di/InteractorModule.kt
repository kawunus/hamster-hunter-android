package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import ru.practicum.android.diploma.search.domain.api.VacanciesSearchInteractor
import ru.practicum.android.diploma.search.domain.impl.VacanciesSearchInteractorImpl
import ru.practicum.android.diploma.vacancy.domain.impl.GetVacancyUseCaseImpl
import ru.practicum.android.diploma.vacancy.domain.usecase.GetVacancyUseCase

val interactorModule = module {
    factoryOf(::VacanciesSearchInteractorImpl) { bind<VacanciesSearchInteractor>() }
    factoryOf(::GetVacancyUseCaseImpl) { bind<GetVacancyUseCase>() }
}
