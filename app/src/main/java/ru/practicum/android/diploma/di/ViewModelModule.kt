package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.presentation.viewmodel.FavoritesViewModel
import ru.practicum.android.diploma.filter.presentation.viewmodel.AreaViewModel
import ru.practicum.android.diploma.filter.presentation.viewmodel.CountryViewModel
import ru.practicum.android.diploma.filter.presentation.viewmodel.FieldViewModel
import ru.practicum.android.diploma.filter.presentation.viewmodel.FilterViewModel
import ru.practicum.android.diploma.filter.presentation.viewmodel.RegionViewModel
import ru.practicum.android.diploma.search.presentation.viewmodel.SearchViewModel
import ru.practicum.android.diploma.vacancy.presentation.viewmodel.VacancyViewModel

val viewModelModule = module {
    viewModelOf(::SearchViewModel)

    viewModelOf(::VacancyViewModel)

    viewModelOf(::FavoritesViewModel)

    viewModelOf(::FilterViewModel)

    viewModelOf(::FieldViewModel)

    viewModel { AreaViewModel(get(), get()) }

    viewModelOf(::RegionViewModel)

    viewModel { CountryViewModel(get(), get()) }
}
