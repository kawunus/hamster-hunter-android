package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.search.domain.api.VacanciesSearchInteractor
import ru.practicum.android.diploma.search.domain.impl.VacanciesSearchInteractorImpl

val interactorModule = module {
    factory<VacanciesSearchInteractor> {
        VacanciesSearchInteractorImpl(repository = get())
    }
}
