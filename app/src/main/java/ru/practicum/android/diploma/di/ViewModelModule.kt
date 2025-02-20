package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.practicum.android.diploma.search.presentation.viewmodel.SearchViewModel

val viewModelModule = module {
    viewModelOf(::SearchViewModel)
}
